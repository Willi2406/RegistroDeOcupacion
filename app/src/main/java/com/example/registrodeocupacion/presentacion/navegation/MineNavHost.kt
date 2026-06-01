package com.example.registrodeocupacion.presentacion.navegation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.registrodeocupacion.presentacion.empleado.adaptative.EmpleadoAdaptiveScreen
import com.example.registrodeocupacion.presentacion.horasextras.adaptative.HoraExtraAdaptiveScreen
import com.example.registrodeocupacion.presentacion.ocupaciones.adaptative.OcupacionAdaptiveScreen

@Composable
fun MainNavigationHost(
    navController: NavHostController = rememberNavController(),
    innerPadding: PaddingValues = PaddingValues()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.OcupacionList,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable<Screen.OcupacionList> {
            OcupacionAdaptiveScreen()
        }
        composable<Screen.EmpleadoList> {
            EmpleadoAdaptiveScreen()
        }
        composable<Screen.HorasExtrasList> {
            HoraExtraAdaptiveScreen()
        }
    }
}