package com.dhamaddam.aplikasipenggunagithubs3.view

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dhamaddam.aplikasipenggunagithubs3.R
import com.dhamaddam.aplikasipenggunagithubs3.utils.SettingPreferences
import com.dhamaddam.aplikasipenggunagithubs3.view.adapter.ViewModeThemeFactory
import com.dhamaddam.aplikasipenggunagithubs3.view.model.ThemeViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class ThemeSettingActivity : AppCompatActivity(){
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_setting)

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)


        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = "Theme Setting"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModeThemeFactory(pref)).get(
            ThemeViewModel::class.java
        )
        mainViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            })

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}