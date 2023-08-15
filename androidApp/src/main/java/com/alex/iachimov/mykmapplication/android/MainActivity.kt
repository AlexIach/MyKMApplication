package com.alex.iachimov.mykmapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    // Injecting MainViewModel into Activity component using viewModel() delegate
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Compose UI setup
        setContent {
            MyApplicationTheme {
                MainScreen(viewModel)
            }
        }
    }
}