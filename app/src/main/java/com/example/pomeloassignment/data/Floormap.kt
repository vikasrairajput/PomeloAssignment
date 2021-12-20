package com.example.pomeloassignment.data
import com.google.gson.annotations.SerializedName

data class Floormap (
	@SerializedName("main") val main : String,
	@SerializedName("zoomed") val zoomed : String
)