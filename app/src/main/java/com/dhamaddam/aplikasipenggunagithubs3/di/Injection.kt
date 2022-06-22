package com.dhamaddam.aplikasipenggunagithubs3.di

import android.content.Context
import com.dhamaddam.aplikasipenggunagithubs3.data.UserRepository
import com.dhamaddam.aplikasipenggunagithubs3.data.local.room.UserDatabase
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.retrofit.ApiConfig
import com.dhamaddam.aplikasipenggunagithubs3.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.newsDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, dao, appExecutors)
    }
}