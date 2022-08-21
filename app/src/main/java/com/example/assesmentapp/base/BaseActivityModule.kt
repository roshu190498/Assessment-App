package com.example.assesmentapp.base

import android.app.Dialog
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object BaseActivityModule {

    @Provides
    fun getProgressBar(@ActivityContext context: Context): Dialog =Utility.showCommonProgressDialog(context)

}