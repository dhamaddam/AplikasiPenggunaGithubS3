package com.dhamaddam.aplikasipenggunagithubs3.view

import androidx.appcompat.app.AppCompatActivity
import com.dhamaddam.aplikasipenggunagithubs3.databinding.ActivityUserFavoriteBinding

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhamaddam.aplikasipenggunagithubs3.data.Result
import com.dhamaddam.aplikasipenggunagithubs3.view.adapter.UserListAdapter
import com.dhamaddam.aplikasipenggunagithubs3.view.adapter.ViewModelFactory
import com.dhamaddam.aplikasipenggunagithubs3.view.model.UserViewModel


class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFavoriteBinding

    private lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = "Favorite User"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userViewModel = obtainViewModel(this@UserFavoriteActivity)


        adapter = UserListAdapter()

        userViewModel.getFavoriteUser().observe(this, { favoriteUser ->
            binding?.progressBarFav?.visibility = View.GONE
            adapter.setListUser(favoriteUser)
        })

        binding?.rvUserFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvUserFavorite?.setHasFixedSize(true)
        binding?.rvUserFavorite?.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserViewModel::class.java)
    }
}