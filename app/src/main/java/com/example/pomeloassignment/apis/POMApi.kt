package com.example.pomeloassignment.apis


import com.example.pomeloassignment.data.ResponseDataModel
import retrofit2.Response
import retrofit2.http.GET

interface POMApi {

    @GET("https://45434c1b-1e22-4af2-8c9f-c2d99ffa4896.mock.pstmn.io/v3/pickup-locations/")
    suspend fun getPickupLocations(): Response<ResponseDataModel>
}