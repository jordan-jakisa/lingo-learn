package com.kerustudios.lingolearn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kerustudios.lingolearn.ui.pages.AuthScreen
import com.kerustudios.lingolearn.ui.pages.HomeScreen
import com.kerustudios.lingolearn.ui.pages.OnBoardingScreen
import com.kerustudios.lingolearn.ui.pages.PracticeScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val startDestination = if (Firebase.auth.currentUser != null) HomePage else OnBoardingPage

    NavHost(navController = navController, startDestination = startDestination) {
        composable<HomePage> {
            HomeScreen(navController = navController)
        }

        composable<PracticePage> {
            val practicePage = it.toRoute<PracticePage>()
            PracticeScreen(
                navController = navController,
                topic = practicePage.topic
            )
        }
        composable<AuthPage> {
            AuthScreen(navController)
        }

        composable<OnBoardingPage> {
            OnBoardingScreen(navController, modifier)
        }
    }
}


// destinations
@Serializable
data object HomePage

@Serializable
data class PracticePage(val topic: String?)

@Serializable
data object AuthPage

@Serializable
data object OnBoardingPage