package com.example.assesmentapp.signin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assesmentapp.base.ResponseData
import com.example.assesmentapp.base.setError
import com.example.assesmentapp.base.setLoading
import com.example.assesmentapp.base.setSuccess
import com.example.assesmentapp.home.model.EmployeeDataModel
import com.example.assesmentapp.signin.model.SignInApiRequestModel
import com.example.assesmentapp.signin.model.SignInApiResponseModel
import com.example.assesmentapp.signin.repository.SignInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val signInRepository: SignInRepository) : ViewModel() {


    val loginApisResponse = MutableLiveData<ResponseData<SignInApiResponseModel>>()
    fun loginApis(requestBody : SignInApiRequestModel) {
        loginApisResponse.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            signInRepository.loginApis(
                requestBody,
                {success-> loginApisResponse.setSuccess(success)},
                {error-> loginApisResponse.setError(error)})
        }
    }


    fun validateEmailPassword(email:String,password:String) : Boolean{
        var flag = false

        if (!email.isNullOrBlank())
            flag = true
        if (!password.isNullOrBlank())
            flag = true
        return flag
    }
}
