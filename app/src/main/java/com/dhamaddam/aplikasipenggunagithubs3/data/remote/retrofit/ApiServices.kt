package com.dhamaddam.aplikasipenggunagithubs3.data.remote.retrofit

import com.dhamaddam.aplikasipenggunagithubs3.data.remote.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @GET("/users")
    fun getAllUser(@Header("Authorization") token: String): Call<ArrayList<GithubResponseItem>>

    @GET("/search/users?q=")
    fun searchAllUser(
        @Header("Authorization") token: String,
        @Query("q") q:String): Call<SearchUserGithubResponse>

    @GET ("/users/{username}/followers")
    fun getFollower(
        @Header("Authorization") token: String,
        @Path("username") username: String?) : Call <ArrayList<GithubResponseItem>>

    @GET ("/users/{username}")
    fun getDetailsUserGithub(
        @Header("Authorization") token: String,
        @Path("username") username: String) : Call <DetailsItem>

    @GET ("/users/{username}/following")
    fun getFollowing(
        @Header("Authorization") token: String,
        @Path("username") username: String?) : Call<ArrayList<GithubResponseItem>>

}