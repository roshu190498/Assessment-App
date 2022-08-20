package com.example.assesmentapp.home.model

import com.google.gson.annotations.SerializedName

data class EmployeeDataModel (

    @SerializedName("status")
    var status  : String?= null,
    @SerializedName("data")
    var data    : ArrayList<Details>? = null,
    @SerializedName("message")
    var message : String? = null
)


data class Details (

    @SerializedName("id")
    var id : Int?= null,
    @SerializedName("employee_name")
    var employeeName : String? = null,
    @SerializedName("employee_salary")
    var employeeSalary : Int?    = null,
    @SerializedName("employee_age")
    var employeeAge : Int?    = null,
    @SerializedName("profile_image")
    var profileImage : String? = null

)
