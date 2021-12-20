package com.example.pomeloassignment.data
import com.google.gson.annotations.SerializedName

data class ImageDataModel (
	@SerializedName("store") val store : Store,
	@SerializedName("floormap") val floormap : Floormap
)