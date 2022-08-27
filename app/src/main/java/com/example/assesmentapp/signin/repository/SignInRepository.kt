package com.example.assesmentapp.signin.repository

import com.example.assesmentapp.base.enqueue
import com.example.assesmentapp.home.apis.HomeApi
import com.example.assesmentapp.home.model.EmployeeDataModel
import com.example.assesmentapp.signin.api.SignInApis
import com.example.assesmentapp.signin.model.SignInApiRequestModel
import com.example.assesmentapp.signin.model.SignInApiResponseModel
import javax.inject.Inject

class SignInRepository  @Inject constructor(private val signInApis: SignInApis){


    fun loginApis(
        requestBody : SignInApiRequestModel,
        success: (successData: SignInApiResponseModel) -> Unit,
        fail: (error: String) -> Unit ){
        signInApis.loginApis(requestBody).enqueue {
            onResponse={
                it.body()?.let {
                    success.invoke(it)
                }?: kotlin.run {
                    fail.invoke("Something went wrong")
                }
            }
            onFailure={
                fail.invoke("Something went Wrong")
            }
        }
    }

}