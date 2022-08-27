package com.example.assesmentapp.signin.di

import com.example.assesmentapp.base.BaseActivityModule
import com.example.assesmentapp.home.apis.HomeApi
import com.example.assesmentapp.signin.api.SignInApis
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create

@Module(includes = [BaseActivityModule::class])
@InstallIn(SingletonComponent::class)
class SignInModule {
    @Provides
    fun providesSignInApis(retrofit: Retrofit) : SignInApis = retrofit.create(SignInApis::class.java)
}