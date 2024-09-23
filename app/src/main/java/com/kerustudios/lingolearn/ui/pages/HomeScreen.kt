package com.kerustudios.lingolearn.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Scaffold(modifier = Modifier) {
        Column(modifier = Modifier
            .padding(it)
            .padding(16.dp)) {
            Text(text = "Home Screen")
        }
    }

}