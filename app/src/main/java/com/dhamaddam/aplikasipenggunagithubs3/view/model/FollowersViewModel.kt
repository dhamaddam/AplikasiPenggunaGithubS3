package com.dhamaddam.aplikasipenggunagithubs3.view.model


import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhamaddam.aplikasipenggunagithubs3.BuildConfig
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.response.GithubResponseItem
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel()  {


    private var listFollowers = MutableLiveData<ArrayList<GithubResponseItem>>()

    fun setFollower(username: String, context: Context) {

        val client = ApiConfig.getApiService().getFollower("token ${BuildConfig.TOKEN}",username)

        client.enqueue( object : Callback<ArrayList<GithubResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<GithubResponseItem>>,
                response: Response<ArrayList<GithubResponseItem>>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()

                    if (responseBody != null) {

                        var listItems = response.body() as ArrayList<GithubResponseItem>
                        listFollowers.postValue(listItems)
                    }
                } else {
                    var statusCode = response.code()
                    val errorMessage = when (response.code()) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden requests get a higher rate limit"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${response.message()}"
                    }
                    Toast.makeText(context,"Error" + errorMessage, Toast.LENGTH_LONG ).show()

                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }

            }

            override fun onFailure(call: Call<ArrayList<GithubResponseItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }


        })
    }

    fun getFollowers(): LiveData<ArrayList<GithubResponseItem>> = listFollowers

}