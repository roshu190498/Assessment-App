package com.example.assesmentapp.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assesmentapp.base.ResponseData
import com.example.assesmentapp.base.setError
import com.example.assesmentapp.base.setLoading
import com.example.assesmentapp.base.setSuccess
import com.example.assesmentapp.capture.model.UploadImageResponse
import com.example.assesmentapp.home.model.EmployeeDataModel
import com.example.assesmentapp.home.repository.CaptureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CaptureViewModelViewModel @Inject constructor(private val captureRepository: CaptureRepository) : ViewModel(){

    val employeeData = MutableLiveData<ResponseData<EmployeeDataModel>>()
    fun getEmployeeData() {
        employeeData.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            captureRepository.getEmployeeData(
                {success-> employeeData.setSuccess(success)},
                {error-> employeeData.setError(error)})
        }
    }

    val dataforUploadImageResponse = MutableLiveData<ResponseData<UploadImageResponse>>()
    fun uploadImage(
        file : MultipartBody.Part,
        params : MutableMap<String,RequestBody>
    ){
        viewModelScope.launch(Dispatchers.IO) {
            captureRepository.uploadImage(
                file,
                params,
                {success-> dataforUploadImageResponse.setSuccess(success)},
                {error-> dataforUploadImageResponse.setError(error)})
        }

    }


    val dataforUploadImageResponse2 = MutableLiveData<ResponseData<UploadImageResponse>>()
    fun uploadImage(
        file : MultipartBody.Part,
    ){
        viewModelScope.launch(Dispatchers.IO) {
            captureRepository.uploadImage2(
                file,
                {success-> dataforUploadImageResponse2.setSuccess(success)},
                {error-> dataforUploadImageResponse2.setError(error)})
        }

    }
}