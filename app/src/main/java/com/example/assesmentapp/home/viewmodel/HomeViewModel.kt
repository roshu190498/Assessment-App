package com.example.assesmentapp.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assesmentapp.base.ResponseData
import com.example.assesmentapp.base.setError
import com.example.assesmentapp.base.setLoading
import com.example.assesmentapp.base.setSuccess
import com.example.assesmentapp.home.model.EmployeeDataModel
import com.example.assesmentapp.home.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel(){

    fun test() : String{
        return homeRepository.test()
    }

    val employeeData = MutableLiveData<ResponseData<EmployeeDataModel>>()
    fun getEmployeeData() {
        employeeData.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getEmployeeData(
                {success-> employeeData.setSuccess(success)},
                {error-> employeeData.setError(error)})
        }
    }
}