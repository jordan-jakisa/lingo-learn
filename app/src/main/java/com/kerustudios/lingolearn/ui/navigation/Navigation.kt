package com.kerustudios.lingolearn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kerustudios.lingolearn.ui.pages.HomeScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(navController = navController, startDestination = HomePage) {
        composable<HomePage> {
            HomeScreen(navController = navController)
        }

    }
}

// destinations
@Serializable
data object HomePage