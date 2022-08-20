package com.example.assesmentapp.home.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.assesmentapp.R
import com.example.assesmentapp.databinding.ActivityHomeBinding
import com.example.assesmentapp.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var mDatabinding : ActivityHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabinding = DataBindingUtil.setContentView(this,R.layout.activity_home)

        mDatabinding.sampleTest.text = homeViewModel.test()
    }
}