package com.dhamaddam.aplikasipenggunagithubs3.view.model

import androidx.lifecycle.ViewModel
import com.dhamaddam.aplikasipenggunagithubs3.data.UserRepository
import com.dhamaddam.aplikasipenggunagithubs3.data.local.entity.UserEntity

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUser() = userRepository.getUser()

    fun getFavoriteUser() = userRepository.getFavorite()

    fun saveUser(users: UserEntity) {
        userRepository.setFavorite(users, true)
    }

    fun deleteUser(users: UserEntity) {
        userRepository.setFavorite(users, false)
    }

}