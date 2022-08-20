package com.example.assesmentapp.home.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.assesmentapp.R
import com.example.assesmentapp.base.Status
import com.example.assesmentapp.databinding.ActivityHomeBinding
import com.example.assesmentapp.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var mDatabinding : ActivityHomeBinding

    @Inject
    lateinit var progressDialog : Dialog

    private val homeViewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabinding = DataBindingUtil.setContentView(this,R.layout.activity_home)

        homeViewModel.getEmployeeData()

        initObserver()
    }

    private fun initObserver() {
        homeViewModel.employeeData.observe(this@HomeActivity, Observer {
            when(it.status) {
                Status.LOADING -> {
                    progressDialog.show()
                }
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    mDatabinding.sampleTest.text = "Something went wrong"
                }
            }
        })
    }
}