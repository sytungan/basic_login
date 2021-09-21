package com.ok.vinova_test

import android.app.Application
import com.ok.vinova_test.viewmodel.ShareDataViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val viewModelModule = module {
            viewModel { ShareDataViewModel() }
        }

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(viewModelModule)
        }

    }

}