package com.kerustudios.lingolearn.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun PracticeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    topic: String?,
    viewModel: PracticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = topic ?: "No topic selected")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Response: ")
        Text(text = uiState.quiz.toString())
    }
}