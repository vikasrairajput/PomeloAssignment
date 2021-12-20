package com.example.pomeloassignment.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomeloassignment.Resource
import com.example.pomeloassignment.data.PickUpDataModel
import com.example.pomeloassignment.source.DataSource
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivityViewModel(private val dataSource: DataSource,private val coroutineContext: CoroutineContext): ViewModel() {
    companion object
    {
        private val TAG= MainActivityViewModel::class.java.simpleName
    }

    private val pickUpsState = MutableLiveData<Resource<ArrayList<PickUpDataModel>>>()

    fun getPickUpsLiveData():LiveData<Resource<ArrayList<PickUpDataModel>>> = pickUpsState

    fun getPickUpsList(){
        pickUpsState.value= Resource.loading(data = null)
        viewModelScope.launch(context = coroutineContext) {
            val response = dataSource.getPickupLocations()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                       val list=ArrayList<PickUpDataModel>()
                        response.body()?.pickUpList?.forEachIndexed { index, pickUpDataModel ->
                            if(pickUpDataModel.active && (!pickUpDataModel.alias.isBlank() &&
                                        !pickUpDataModel.address1.isBlank() && !pickUpDataModel.city.isBlank())
                            )
                            list.add(pickUpDataModel)
                        }
                    pickUpsState.value =Resource.success(list)
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }
    private fun onError(message: String) {
        pickUpsState.value= Resource.error(null,message)
    }
     fun sortList(lat:Double,long:Double,defaultDispatcher: CoroutineDispatcher){
         viewModelScope.launch(context = defaultDispatcher) {
             val list=sortingWork(lat,long)
             withContext(Dispatchers.Main){
                 pickUpsState.value= Resource.success(list!!)
             }
         }
    }

    private suspend fun sortingWork(lat:Double, long:Double):ArrayList<PickUpDataModel>?{
        val list= pickUpsState.value?.data
        list?.forEach {
            it.distance=distance(lat1 = lat,lon1 = long,lat2 = it.latitude,lon2 = it.longitude)
        }
        list?.sortBy { it.distance }
        return list
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}