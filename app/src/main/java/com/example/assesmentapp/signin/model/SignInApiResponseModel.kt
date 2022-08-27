package com.example.assesmentapp.signin.model

import com.google.gson.annotations.SerializedName

data class SignInApiResponseModel(
    @SerializedName("token")
    val token : String?=null
)