package com.dhamaddam.aplikasipenggunagithubs3.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dhamaddam.aplikasipenggunagithubs3.data.UserRepository
import com.dhamaddam.aplikasipenggunagithubs3.data.local.room.UserDatabase
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.retrofit.ApiConfig
import com.dhamaddam.aplikasipenggunagithubs3.utils.AppExecutors
import com.dhamaddam.aplikasipenggunagithubs3.utils.SettingPreferences

object Injection {
    fun provideRepository(context: Context , dataStore: DataStore<Preferences>): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.newsDao()
        val appExecutors = AppExecutors()
        val preferenDS = SettingPreferences.getInstance(dataStore)
        return UserRepository.getInstance(apiService, dao, appExecutors ,preferenDS)
    }
}