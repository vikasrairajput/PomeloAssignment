package com.example.pomeloassignment.data
import com.google.gson.annotations.SerializedName


data class Primary (
	@SerializedName("landscape") val landscape : String,
	@SerializedName("full_landscape") val full_landscape : String,
	@SerializedName("portrait") val portrait : String,
	@SerializedName("full_portrait") val full_portrait : String
)