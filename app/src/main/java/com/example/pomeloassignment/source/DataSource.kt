package com.example.pomeloassignment.source


import com.example.pomeloassignment.data.ResponseDataModel
import retrofit2.Response

interface DataSource {
    suspend fun getPickupLocations() : Response<ResponseDataModel>
}