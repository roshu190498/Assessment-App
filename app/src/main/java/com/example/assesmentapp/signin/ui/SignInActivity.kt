package com.example.assesmentapp.signin.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.assesmentapp.R
import com.example.assesmentapp.base.Status
import com.example.assesmentapp.base.openActivity
import com.example.assesmentapp.base.showToast
import com.example.assesmentapp.calculator.ui.CalculatorActivity
import com.example.assesmentapp.databinding.ActivitySignInBinding
import com.example.assesmentapp.signin.model.SignInApiRequestModel
import com.example.assesmentapp.signin.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private lateinit var mDatabinding : ActivitySignInBinding

    @Inject
    lateinit var progressDialog : Dialog

    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabinding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in)
        initObserver()
        initClickListners()
    }

    private fun initObserver() {
        signInViewModel.loginApisResponse.observe(this@SignInActivity, Observer {
            when(it.status){
                Status.SUCCESS->{
                    progressDialog?.cancel()
                    openActivity(CalculatorActivity::class.java)
                }
                Status.LOADING->{
                    progressDialog?.show()
                }
                Status.ERROR->{
                    progressDialog?.cancel()
                    showToast(
                        getString(R.string.txtShowLoginError)
                    )
                }
            }
        })
    }

    private fun initClickListners() {
        mDatabinding.btnLogin.setOnClickListener {
            if(signInViewModel.validateEmailPassword(mDatabinding.etEmail.text.toString(),mDatabinding.etPassword.text.toString())){
                var requestBody = SignInApiRequestModel().apply {
                    email=mDatabinding.etEmail.text.toString()
                    password=mDatabinding.etPassword.text.toString()
                }
                signInViewModel.loginApis(
                    requestBody
                )
            }else{
                showToast(getString(R.string.txtValidateMsg))
            }
        }
    }
}