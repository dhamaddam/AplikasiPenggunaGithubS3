package com.dhamaddam.aplikasipenggunagithubs3.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dhamaddam.aplikasipenggunagithubs3.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users where isfavorite = 1")
    fun getFavoritedUser(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(news: List<UserEntity>)

    @Update
    fun updateUser(user: UserEntity)

    @Query("DELETE FROM users WHERE isfavorite = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM users WHERE login = :username AND isfavorite = 1)")
    fun isUserFavorite(username: String): Boolean
}