package com.dhamaddam.aplikasipenggunagithubs3.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dhamaddam.aplikasipenggunagithubs3.utils.SettingPreferences
import kotlinx.coroutines.launch

class ThemeViewModel (private val pref: SettingPreferences) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}