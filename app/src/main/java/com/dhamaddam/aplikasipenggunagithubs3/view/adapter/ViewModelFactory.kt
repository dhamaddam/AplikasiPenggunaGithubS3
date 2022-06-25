package com.dhamaddam.aplikasipenggunagithubs3.view.adapter

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dhamaddam.aplikasipenggunagithubs3.data.UserRepository
import com.dhamaddam.aplikasipenggunagithubs3.di.Injection
import com.dhamaddam.aplikasipenggunagithubs3.view.model.UserViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ViewModelFactory private constructor(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context, context.dataStore))
            }.also { instance = it }
    }
}