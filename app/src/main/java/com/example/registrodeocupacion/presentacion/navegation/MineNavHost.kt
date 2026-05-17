package com.example.registrodeocupacion.presentacion.navegation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.registrodeocupacion.presentacion.ocupaciones.list.OcupacionListScreen
import com.example.registrodeocupacion.presentacion.ocupaciones.edit.OcupacionFormScreen



@Composable
fun MineNavHost(
    navController: NavHostController = rememberNavController(),
    innerPadding: PaddingValues
) {
    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController =  navController,
        startDestination = Screen.OcupacionList
    ) {
        composable<Screen.OcupacionList> {
            OcupacionListScreen(
                onAddOcupacion = {
                    navController.navigate(Screen.OcupacionForm(ocupacionId = 0))
                },
                onEditOcupacion = { id ->
                    navController.navigate(Screen.OcupacionForm(ocupacionId = id))
                }
            )
        }

        composable<Screen.OcupacionForm> {
            OcupacionFormScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}