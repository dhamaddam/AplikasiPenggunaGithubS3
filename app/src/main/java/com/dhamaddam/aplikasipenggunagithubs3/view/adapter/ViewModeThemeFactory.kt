package com.dhamaddam.aplikasipenggunagithubs3.view.adapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dhamaddam.aplikasipenggunagithubs3.utils.SettingPreferences
import com.dhamaddam.aplikasipenggunagithubs3.view.model.ThemeViewModel


class ViewModeThemeFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}