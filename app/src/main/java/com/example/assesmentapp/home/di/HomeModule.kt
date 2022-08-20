package com.example.assesmentapp.home.di

import com.example.assesmentapp.base.BaseActivityModule
import com.example.assesmentapp.home.apis.HomeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module( includes = [BaseActivityModule::class])
@InstallIn(SingletonComponent::class)
class HomeModule {
    @Provides
    fun providesHomeApi(retrofit: Retrofit) : HomeApi = retrofit.create(HomeApi::class.java)

}