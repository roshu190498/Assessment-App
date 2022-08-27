package com.example.assesmentapp.signin.model

import com.google.gson.annotations.SerializedName

data class SignInApiRequestModel(
    @SerializedName("email")
    var email : String?=null,
    @SerializedName("password")
    var password : String?=null
)

