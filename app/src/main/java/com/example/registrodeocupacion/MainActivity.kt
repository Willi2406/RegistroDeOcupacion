package com.example.registrodeocupacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange // ¡Nueva importación para el ícono de Horas Extras!
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.registrodeocupacion.presentacion.navegation.MineNavHost
import dagger.hilt.android.AndroidEntryPoint
import com.example.registrodeocupacion.presentacion.navegation.Screen
import com.example.registrodeocupacion.ui.theme.RegistroDeOcupacionTheme
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroDeOcupacionTheme {
                val navController = rememberNavController()


                val items = listOf(
                    TopLevelRoute("Empleado", Screen.EmpleadoList, Icons.Default.Person),
                    TopLevelRoute("Ocupacion", Screen.OcupacionList, Icons.Default.Home),
                    TopLevelRoute("Horas Extras", Screen.HorasExtrasList, Icons.Default.DateRange)
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            items.forEach { item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icono, contentDescription = item.nombre) },
                                    label = { Text(item.nombre) },
                                    selected = currentDestination?.hierarchy?.any {
                                        it.hasRoute(item.ruta::class)
                                    } == true,
                                    onClick = {
                                        navController.navigate(item.ruta) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MineNavHost(
                        navController = navController,
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}

data class TopLevelRoute<T : Any>(
    val nombre: String,
    val ruta: T,
    val icono: androidx.compose.ui.graphics.vector.ImageVector
)