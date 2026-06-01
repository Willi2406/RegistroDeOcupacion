package com.example.registrodeocupacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.registrodeocupacion.ui.theme.RegistroDeOcupacionTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroDeOcupacionTheme(){
                RegistroOcupacionesAppUI()
            }
        }
    }
}