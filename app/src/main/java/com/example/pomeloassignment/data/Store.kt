package com.example.pomeloassignment.data

import com.google.gson.annotations.SerializedName

data class Store (
	@SerializedName("primary") val primary : Primary,
	@SerializedName("secondary") val secondary : String,
	@SerializedName("full_secondary") val full_secondary : String
)