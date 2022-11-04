package com.example.assesmentapp.home.di

import com.example.assesmentapp.base.BaseActivityModule
import com.example.assesmentapp.home.apis.CaptureApis
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module( includes = [BaseActivityModule::class])
@InstallIn(SingletonComponent::class)
class CaptureModule {
    @Provides
    fun providesCaptureApis(retrofit: Retrofit) : CaptureApis = retrofit.create(CaptureApis::class.java)

}