package com.example.postrequestpractice

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.Call

interface APIInterface {

    @GET("test/")
    fun getNamesLocations(): Call<NamesAndLocations>

    @POST("test/")
    fun addNameLocation(@Body userNameLocation: NamesAndLocationsItem): Call<NamesAndLocationsItem>
// "userNameLocation" is the parameter that I want to pass (what will be posted in the server) and it's of type NamesAndLocationsItem
}