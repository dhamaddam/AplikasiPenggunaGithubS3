package com.dhamaddam.aplikasipenggunagithubs3.view

import android.app.SearchManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhamaddam.aplikasipenggunagithubs3.BuildConfig
import com.dhamaddam.aplikasipenggunagithubs3.R
import com.dhamaddam.aplikasipenggunagithubs3.databinding.ActivityMainBinding
import com.dhamaddam.aplikasipenggunagithubs3.view.adapter.UserListAdapter
import com.dhamaddam.aplikasipenggunagithubs3.view.adapter.ViewModelFactory
import com.dhamaddam.aplikasipenggunagithubs3.view.model.UserViewModel

import com.dhamaddam.aplikasipenggunagithubs3.data.Result
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.response.GithubResponseItem
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.response.SearchUserGithubResponse
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.retrofit.ApiConfig
import com.dhamaddam.aplikasipenggunagithubs3.view.adapter.ListUserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding

    private lateinit var adapter: UserListAdapter
    private var list: ArrayList<GithubResponseItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val userViewModel = obtainViewModel(this@MainActivity)



        adapter = UserListAdapter()

        userViewModel.getUser().observe(this, { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        val userData = result.data
                        adapter.setListUser(userData)
                    }
                    is Result.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(
                            applicationContext,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })

        binding?.rvGithuber?.layoutManager = LinearLayoutManager(this)
        binding?.rvGithuber?.setHasFixedSize(true)
        binding?.rvGithuber?.adapter = adapter

    }

    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserViewModel::class.java)
    }

    private fun SearchUserGithub(username:String)
    {
        binding?.progressBar?.visibility = View.VISIBLE

        val client = ApiConfig.getApiService().searchAllUser("token ${BuildConfig.TOKEN}", username)

        client.enqueue( object : Callback <SearchUserGithubResponse> {
            override fun onResponse(
                call: Call<SearchUserGithubResponse>,
                response: Response<SearchUserGithubResponse>
            ) {
                binding?.progressBar?.visibility = View.GONE

                if (response.isSuccessful) {

                    val responseBody = response.body()

                    if (responseBody != null) {
                        list.clear()
                        list = response.body()?.items as ArrayList<GithubResponseItem>
                        showRecyclerList()

                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }

            }

            override fun onFailure(call: Call<SearchUserGithubResponse>, t: Throwable) {
                binding?.progressBar?.visibility = View.GONE
                val errorMessage = t.message
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
            }


        })
    }

    private fun showRecyclerList() {
         val listUserAdapter = ListUserAdapter(list)

        binding?.rvGithuber?.layoutManager = LinearLayoutManager(this)
        binding?.rvGithuber?.setHasFixedSize(true)
        binding?.rvGithuber?.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubResponseItem) {
                showSelectedUser(data)
            }
        })

    }

    private fun showSelectedUser(user: GithubResponseItem) {
        val startGithubUserDetails = Intent(this@MainActivity, GithubUserDetailsActivity::class.java)
        startGithubUserDetails.putExtra(GithubUserDetailsActivity.EXTRA_USER, user.username)
        startGithubUserDetails.putExtra(GithubUserDetailsActivity.EXTRA_USER_FAVORITE, user.gistsUrl)
        startActivity(startGithubUserDetails)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView = menu.findItem(R.id.search).actionView as SearchView
        var menuSearch = menu.findItem(R.id.search)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                SearchUserGithub(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }


        })

        val expandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {

                binding?.rvGithuber?.layoutManager = LinearLayoutManager(applicationContext)
                binding?.rvGithuber?.setHasFixedSize(true)
                binding?.rvGithuber?.adapter = adapter
                return true // Return true to collapse action view
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // Do something when expanded
                return true // Return true to expand action view
            }
        }



        var FavoriteButton = menu.findItem(R.id.action_favorite)
        FavoriteButton.setOnMenuItemClickListener ( object : MenuItem.OnMenuItemClickListener  {
            override fun onMenuItemClick(item: MenuItem?): Boolean {

                val startGithubUserDetails = Intent(applicationContext, UserFavoriteActivity::class.java)
                startActivity(startGithubUserDetails)

                return true
            }

        } )

        var SettingButton = menu.findItem(R.id.action_theme)
        SettingButton.setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener  {
            override fun onMenuItemClick(item: MenuItem?): Boolean {

                val startthemeSettingActivity = Intent(applicationContext, ThemeSettingActivity::class.java)
                startActivity(startthemeSettingActivity)

                return true
            }

        } )


        menuSearch?.setOnActionExpandListener(expandListener)

        return true
    }
}

