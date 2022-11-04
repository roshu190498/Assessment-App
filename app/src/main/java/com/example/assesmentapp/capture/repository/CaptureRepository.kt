package com.example.assesmentapp.home.repository

import com.example.assesmentapp.base.enqueue
import com.example.assesmentapp.capture.model.UploadImageResponse
import com.example.assesmentapp.home.apis.CaptureApis
import com.example.assesmentapp.home.apis.HomeApi
import com.example.assesmentapp.home.model.EmployeeDataModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject



class CaptureRepository @Inject constructor(private val captureApis: CaptureApis){

    fun test() : String{
        return "Test"
    }

    fun getEmployeeData(
        success: (employeeDataModels: EmployeeDataModel) -> Unit,
        fail: (error: String) -> Unit ){
        captureApis.getEmployeeData().enqueue {
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


    fun uploadImage(
        file: MultipartBody.Part,
        params : MutableMap<String,RequestBody>,
        success: (uploadImageResponse: UploadImageResponse) -> Unit,
        fail: (error: String) -> Unit ){
        captureApis.uploadImage(file,params).enqueue{
            onResponse={
                it.body()?.let {
                    success.invoke(it)
                }?: kotlin.run {
                    fail.invoke("Something went wrong")
                }
            }
            onFailure={
                fail.invoke("Something went Wrong ${it.toString()}")
            }
        }
    }

    fun uploadImage2(
        file: MultipartBody.Part,
        success: (uploadImageResponse: UploadImageResponse) -> Unit,
        fail: (error: String) -> Unit ){
        captureApis.uploadImage(file).enqueue{
            onResponse={
                it.body()?.let {
                    success.invoke(it)
                }?: kotlin.run {
                    fail.invoke("Something went wrong")
                }
            }
            onFailure={
                fail.invoke("Something went Wrong ${it.toString()}")
            }
        }
    }

}