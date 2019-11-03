package com.example.businessapp.application.injection.components

import androidx.lifecycle.ViewModelProvider
import com.example.businessapp.application.MyApp
import com.example.businessapp.application.injection.modules.ActivityModule
import com.example.businessapp.application.injection.modules.ApplicationModule
import com.example.businessapp.application.injection.modules.MainModule
import com.example.businessapp.application.injection.modules.NetworkModule
import com.example.businessapp.application.injection.modules.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    ActivityModule::class,
    NetworkModule::class,
    MainModule::class,
    ViewModelModule::class])
interface AppComponent {

    fun inject(app: MyApp)

    // ViewModel factory
    fun viewModelFactory(): ViewModelProvider.Factory
}