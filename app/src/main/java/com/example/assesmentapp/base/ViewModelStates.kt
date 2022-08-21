package com.example.assesmentapp.base

import androidx.lifecycle.MutableLiveData

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class ResponseData<out T>(val status: Status, val data: T?=null, val message: String?=null)

fun <T> MutableLiveData<ResponseData<T>>.setSuccess(data: T?=null, msg: String?=null) {
    value = ResponseData(Status.SUCCESS, data, msg)
}
fun <T> MutableLiveData<ResponseData<T>>.setLoading(data: T?=null) {
    value = ResponseData(Status.LOADING, data, null)
}
fun <T> MutableLiveData<ResponseData<T>>.setError(msg: String?=null, data: T?=null) {
    value = ResponseData(Status.ERROR, data, msg)
}
