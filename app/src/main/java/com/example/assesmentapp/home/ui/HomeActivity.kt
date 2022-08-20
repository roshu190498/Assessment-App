package com.example.assesmentapp.home.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.assesmentapp.R
import com.example.assesmentapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var mDatabinding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabinding = DataBindingUtil.setContentView(this,R.layout.activity_home)
    }
}