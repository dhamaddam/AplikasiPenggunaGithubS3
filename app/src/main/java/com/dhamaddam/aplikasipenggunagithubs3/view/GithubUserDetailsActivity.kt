package com.dhamaddam.aplikasipenggunagithubs3.view

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dhamaddam.aplikasipenggunagithubs3.BuildConfig
import com.dhamaddam.aplikasipenggunagithubs3.R
import com.dhamaddam.aplikasipenggunagithubs3.data.local.entity.UserEntity
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.response.DetailsItem
import com.dhamaddam.aplikasipenggunagithubs3.data.remote.retrofit.ApiConfig
import com.dhamaddam.aplikasipenggunagithubs3.databinding.ActivityGithubUserDetailsBinding
import com.dhamaddam.aplikasipenggunagithubs3.view.adapter.SectionsPagerAdapter
import com.dhamaddam.aplikasipenggunagithubs3.view.adapter.ViewModelFactory
import com.dhamaddam.aplikasipenggunagithubs3.view.model.UserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubUserDetailsActivity : AppCompatActivity()   {

    private lateinit var binding: ActivityGithubUserDetailsBinding

    private lateinit var username : String

    private var isFavoriteFlagged = false

    private lateinit var favoriteUser : UserEntity

    private var id : Int = 0

    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    private lateinit var userList  : ArrayList<UserEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGithubUserDetailsBinding.inflate(layoutInflater)
        username = intent.getStringExtra(EXTRA_USER).toString()

        isFavoriteFlagged = intent.getStringExtra(EXTRA_USER_FAVORITE).toBoolean()
        DetailsUserGithub(username)

        Log.d(TAG, "isFavoriteFlagged from outside: "+ isFavoriteFlagged)

        setFavoriteStatus(isFavoriteFlagged)

        val userViewModel = obtainViewModel(this@GithubUserDetailsActivity)

        favoriteUser = UserEntity(null,"dhamaddam","","",true)

        userList = ArrayList<UserEntity>()

        binding.fabFavorite.setOnClickListener {

            if (!isFavoriteFlagged) {
                userViewModel.removeUserFavorite(username)
                userViewModel.addUserFavorite(userList)
                isFavoriteFlagged = !isFavoriteFlagged
                setFavoriteStatus(isFavoriteFlagged)
                Log.d(TAG, "!isFavoriteFlagged: "+ isFavoriteFlagged)
            } else {
                favoriteUser.isfavorite = false
                userViewModel.deleteUser(favoriteUser)
                setFavoriteStatus(!isFavoriteFlagged)
                Log.d(TAG, "isFavoriteFlagged: "+ isFavoriteFlagged)
            }
        }
        setContentView(binding.root)

        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = "Detail Pengguna"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabFollowerFollowing

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserViewModel::class.java)
    }

    private fun DetailsUserGithub(username:String)
    {
        showLoading(true)

        val client = ApiConfig.getApiService().getDetailsUserGithub("token ${BuildConfig.TOKEN}",username)

        client.enqueue( object : Callback<DetailsItem> {
            override fun onResponse(
                call: Call<DetailsItem>,
                response: Response<DetailsItem>
            ) {
                showLoading(false)

                if (response.isSuccessful) {

                    val responseBody = response.body()

                    if (responseBody != null) {
                        setUserDetailsData(responseBody)

                        favoriteUser.login = responseBody.login
                        favoriteUser.avatar = responseBody.avatarUrl
                        favoriteUser.id = responseBody.id
                        favoriteUser = UserEntity (
                            responseBody.id,
                            responseBody.login,
                            responseBody.type,
                            responseBody.avatarUrl,
                            true
                        )
                        userList.add(favoriteUser)
                        Log.d(TAG, "onResponse: isFavoriteFlagged "+responseBody)

                    }
                } else {
                    var statusCode = response.code()
                    val errorMessage = when (response.code()) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden requests get a higher rate limit"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${response.message()}"
                    }
                    Toast.makeText(applicationContext, "Error$errorMessage", Toast.LENGTH_LONG ).show()

                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }

            }

            override fun onFailure(call: Call<DetailsItem>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                val errorMessage = t.message
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
            }


        })
    }


    private fun setUserDetailsData(Items: DetailsItem) {

        binding.apply {
            tvNameReceived.text = "Name : " + Items.name
            tvRepository.text = "Repository : " +Items.publicRepos
            tvFollowing.text = "Following : " +Items.following
            tvFollower.text = "Followers : " + Items.followers
            tvCompany.text = "Company : " +Items.company
            tvLocation.text = "Location : " + Items.location

        }
        Glide.with(this).load(Items.avatarUrl).into(binding.ivAvatarReceived)
    }






    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }



    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_USER_FAVORITE = "extra_user_favorite"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }


    private fun setFavoriteStatus(state: Boolean) {
        if (state) binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
        else binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
    }


}