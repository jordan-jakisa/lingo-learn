package com.kerustudios.lingolearn.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kerustudios.lingolearn.data.models.Language
import com.kerustudios.lingolearn.ui.navigation.HomePage
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    vm: OnBoardingScreenViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var selectedLanguage by remember { mutableStateOf<Language?>(null) }
    var goals: Set<String> = mutableSetOf()
    val pagerState = rememberPagerState { 2 }

    val pages: List<@Composable () -> Unit> =
        listOf(languagePreference(modifier = Modifier.fillMaxWidth()) {
            selectedLanguage = it
            scope.launch {
                pagerState.animateScrollToPage(1)
            }
        }, languageGoals(
            modifier = Modifier.fillMaxWidth(), language = selectedLanguage?.name ?: ""
        ) {
            goals = it
            vm.updatePreferences(selectedLanguage!!, goals).also {
                navController.navigate(HomePage)
            }
        }
        )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 36.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LinearProgressIndicator(
            progress = {
                when (pagerState.currentPage) {
                    0 -> 0.5f
                    else -> 1f
                }
            }, modifier = Modifier.fillMaxWidth()
        )
        HorizontalPager(state = pagerState) { index ->
            pages[index]()
        }
    }


}

@Composable
fun languagePreference(
    modifier: Modifier = Modifier, onSelect: (Language) -> Unit
): @Composable () -> Unit {
    return {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "I want to learn",
                modifier = Modifier,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(
                    items = listOf(
                        Language("🇪🇸", "Spanish"),
                        Language("🇫🇷", "French"),
                        Language("🇩🇪", "German"),
                        Language("🇮🇹", "Italian"),
                        Language("🇯🇵", "Japanese"),
                    )
                ) {
                    ElevatedCard(onClick = { onSelect(it) }) {
                        Row(modifier = modifier.padding(16.dp)) {
                            Text(text = it.emoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = it.name)
                        }
                    }
                }

            }

        }
    }
}

@Composable
fun languageGoals(
    modifier: Modifier = Modifier, language: String, onSave: (Set<String>) -> Unit
): @Composable () -> Unit {
    return {
        val selectedGoals = mutableSetOf<String>()
        val options = listOf(
            "Master the basics",
            "Chat with the native $language speakers",
            "Watch a movie in $language",
            "Learn about the $language culture",
            "Connect with family and friends",
            "Impress my colleagues",
            "Ace my next $language test"
        )
        Column(
            modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "What are your goals?", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text(text = "Select all that apply", fontSize = 12.sp, modifier = Modifier.alpha(.5f))
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = options) {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = if (selectedGoals.contains(it))
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.Transparent
                            ),
                        onClick = {
                            if (selectedGoals.contains(it))
                                selectedGoals.remove(it)
                            else
                                selectedGoals.add(it)
                        }
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier
                                .padding(16.dp),
                        )
                    }
                }
            }
            Button(
                onClick = {
                    onSave(selectedGoals.toSet())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 36.dp)
            ) {
                Text(text = "Continue")
            }
        }
    }

}
