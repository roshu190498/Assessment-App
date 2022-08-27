package com.example.assesmentapp.signin.api

import com.example.assesmentapp.home.model.EmployeeDataModel
import com.example.assesmentapp.signin.model.SignInApiRequestModel
import com.example.assesmentapp.signin.model.SignInApiResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/*
Api call to be added here
 */
interface SignInApis {

    /**
     * POST Login Api Call
     */
    @POST("api/login")
    fun loginApis(@Body singinApiRequestModel: SignInApiRequestModel): Call<SignInApiResponseModel>

}