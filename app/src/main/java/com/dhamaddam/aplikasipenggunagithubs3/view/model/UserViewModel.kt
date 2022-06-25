package com.dhamaddam.aplikasipenggunagithubs3.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dhamaddam.aplikasipenggunagithubs3.data.UserRepository
import com.dhamaddam.aplikasipenggunagithubs3.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUser() = userRepository.getUser()

    fun getFavoriteUser() = userRepository.getFavorite()

    fun saveUser(users: UserEntity) {
        userRepository.setFavorite(users, true)
    }
    fun getThemeSettings(): LiveData<Boolean> {
        return userRepository.getThemeSetting().asLiveData()
    }

    fun deleteUser(users: UserEntity) {
        userRepository.setFavorite(users, false)
    }
    fun removeUserFavorite (username: String){
        userRepository.RemoveUserFavorite(username)
    }
    fun addUserFavorite (users: ArrayList<UserEntity>){
        userRepository.AddUserFavorite(users)
    }

    init {
        viewModelScope.launch{

        }

    }

}