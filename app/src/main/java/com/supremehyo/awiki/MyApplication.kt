package com.supremehyo.awiki

import android.app.Application
import dagger.hilt.android.HiltAndroidApp



@HiltAndroidApp//힐트를 사용하는 첫번째 시작점
class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()
    }
}