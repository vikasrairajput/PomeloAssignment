package com.example.pomeloassignment.data
import com.google.gson.annotations.SerializedName

data class PaymentMethod (

	@SerializedName("id_partner_store") val id_partner_store : Int,
	@SerializedName("id_payment_type") val id_payment_type : Int,
	@SerializedName("description") val description : String,
	@SerializedName("active") val active : Int,
	@SerializedName("position") val position : Int,
	@SerializedName("is_new") val is_new : Boolean
)