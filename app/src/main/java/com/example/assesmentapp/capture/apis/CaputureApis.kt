package com.example.assesmentapp.home.apis

import com.example.assesmentapp.capture.model.UploadImageResponse
import com.example.assesmentapp.home.model.EmployeeDataModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/*
Api call to be added here
 */
interface CaptureApis {

    /**
     * Get Employeee Data API
     */
    @GET("61cf7d91-a7f8-405e-b505-67926b759d78")
    fun getEmployeeData(): Call<EmployeeDataModel>


    @Headers("X-Api-Key: dbJ5AzPIR93x6NLPUHZok5TiGptpfNAB1oEwSXUe")
    @POST("/upload")
    @Multipart
    fun uploadImage(@Part file : MultipartBody.Part, @PartMap part : MutableMap<String,RequestBody>) : Call<UploadImageResponse>


    @POST("3/upload")
    @Multipart
    fun uploadImage(@Part file : MultipartBody.Part) : Call<UploadImageResponse>
}