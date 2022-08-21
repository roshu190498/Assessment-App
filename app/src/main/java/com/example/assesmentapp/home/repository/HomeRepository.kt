package com.example.assesmentapp.home.repository

import com.example.assesmentapp.base.enqueue
import com.example.assesmentapp.home.apis.HomeApi
import com.example.assesmentapp.home.model.EmployeeDataModel
import javax.inject.Inject

class HomeRepository @Inject constructor(private val homeApis: HomeApi){

    fun test() : String{
        return "Test"
    }

    fun getEmployeeData(
        success: (employeeDataModels: EmployeeDataModel) -> Unit,
        fail: (error: String) -> Unit ){
        homeApis.getEmployeeData().enqueue {
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