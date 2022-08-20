package com.example.assesmentapp.home.apis

import com.example.assesmentapp.home.model.EmployeeDataModel
import retrofit2.Call
import retrofit2.http.GET

/*
Api call to be added here
 */
interface HomeApi {

    /**
     * Get Employeee Data API
     */
    @GET("61cf7d91-a7f8-405e-b505-67926b759d78")
    fun getEmployeeData(): Call<EmployeeDataModel>
}