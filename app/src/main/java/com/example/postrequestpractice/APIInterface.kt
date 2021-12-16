package com.example.postrequestpractice

import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @GET("test/")
    fun getNamesLocations(): Call<NamesAndLocations>

    @POST("test/")
    fun addNameLocation(@Body userNameLocation: NamesAndLocationsItem): Call<NamesAndLocationsItem>
// "userNameLocation" is the parameter that I want to pass (what will be posted in the server) and it's of type NamesAndLocationsItem

    //PUT replaces the full object (use PATCH to update individual fields)
    @PUT("test/{id}") // here we paas in the ID of the post we want to update
    fun updateNameLocation(@Path("id") id: Int, @Body userNameLocation: NamesAndLocationsItem): Call<NamesAndLocationsItem>

    @DELETE("test/{id}")
    fun deleteNameLocation(@Path("id") id: Int): Call<Void>
    // we use Void to overwrite an existing post
}