package com.example.assesmentapp.home.viewmodel

import androidx.lifecycle.ViewModel
import com.example.assesmentapp.home.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel(){

    fun test() : String{
        return homeRepository.test()
    }
}