package com.dhamaddam.aplikasipenggunagithubs3.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dhamaddam.aplikasipenggunagithubs3.BuildConfig
import com.dhamaddam.aplikasipenggunagithubs3.data.local.entity.UserEntity
import com.dhamaddam.aplikasipenggunagithubs3.data.local.room.UserDao
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.response.GithubResponseItem
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.response.SearchUserGithubResponse
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.retrofit.ApiServices
import com.dhamaddam.aplikasipenggunagithubs3.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiServices,
    private val userDao: UserDao,
    private val appExecutors: AppExecutors
) {

    private val result = MediatorLiveData<Result<List<UserEntity>>>()

    fun getUser(): LiveData<Result<List<UserEntity>>> {
        result.value = Result.Loading
        val client = apiService.getAllUser("token ${BuildConfig.TOKEN}")
        client.enqueue(object : Callback <ArrayList<GithubResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<GithubResponseItem>>,
                response: Response<ArrayList<GithubResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val users = response.body() as ArrayList<GithubResponseItem>
                    val userList = ArrayList<UserEntity>()
                    appExecutors.diskIO.execute {
                        users?.forEach { users ->
                            val isFavorite = userDao.isUserFavorite(users.username.toString())
                            val news = UserEntity(
                                users.id,
                                users.username,
                                users.type,
                                users.avatar,
                                isFavorite
                            )
                            userList.add(news)
                        }
                        userDao.deleteAll()
                        userDao.insertUser(userList)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<GithubResponseItem>>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = userDao.getUsers()
        result.addSource(localData) { newData: List<UserEntity> ->
            result.value = Result.Success(newData)
        }
        return result
    }

    fun getFavorite(): LiveData<List<UserEntity>> {
        return userDao.getFavoritedUser()
    }


    fun setFavorite(users: UserEntity, favoriteState: Boolean) {
        appExecutors.diskIO.execute {
            users.isfavorite = favoriteState
            userDao.updateUser(users)
        }
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiServices,
            newsDao: UserDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, newsDao, appExecutors)
            }.also { instance = it }
    }


}