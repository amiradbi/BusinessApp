package com.example.businessapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.businessapp.R
import dagger.android.AndroidInjection


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}