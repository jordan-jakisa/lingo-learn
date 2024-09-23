package com.kerustudios.lingolearn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kerustudios.lingolearn.ui.pages.HomeScreen
import com.kerustudios.lingolearn.ui.pages.PracticeScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(navController = navController, startDestination = HomePage) {
        composable<HomePage> {
            HomeScreen(navController = navController)
        }

        composable<PracticePage> {
            val practicePage = it.toRoute<PracticePage>()
            PracticeScreen(navController = navController, topic = practicePage.topic)
        }

    }
}

// destinations
@Serializable
data object HomePage

@Serializable
data class PracticePage(val topic: String?)