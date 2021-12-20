package com.example.pomeloassignment.data

import com.google.gson.annotations.SerializedName

data class ResponseDataModel(
    @SerializedName("number_of_new_locations")
    val number_of_new_locations:String="",
    @SerializedName("pickup")
    var pickUpList:ArrayList<PickUpDataModel> = ArrayList()
    ) {
}