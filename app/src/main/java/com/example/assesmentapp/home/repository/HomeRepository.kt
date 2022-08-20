package com.example.assesmentapp.home.repository

import com.example.assesmentapp.home.apis.HomeApi
import javax.inject.Inject

class HomeRepository @Inject constructor(private val homeApis: HomeApi){

    fun test() : String{
        return "Test"
    }
}