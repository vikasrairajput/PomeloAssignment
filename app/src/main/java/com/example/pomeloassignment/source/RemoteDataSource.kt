package com.example.pomeloassignment.source

import com.example.pomeloassignment.apis.POMService
import com.example.pomeloassignment.data.ResponseDataModel
import retrofit2.Response

class RemoteDataSource: DataSource {

    companion object{
        private val TAG= RemoteDataSource::class.java.simpleName
    }

    private val mmtService = POMService().getPOMService()


    override suspend fun getPickupLocations(): Response<ResponseDataModel> {
       return mmtService.getPickupLocations()
    }
}