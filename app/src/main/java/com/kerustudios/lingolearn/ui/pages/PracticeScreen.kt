package com.kerustudios.lingolearn.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kerustudios.lingolearn.data.models.QuizQuestion
import com.kerustudios.lingolearn.utils.isCorrect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    topic: String?,
    viewModel: PracticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Practice") }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = ""
                )
            }
        })
    }) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (uiState.pageState) {
                    PageState.Loading -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            CircularProgressIndicator()

                            /*CircledDotsProgress(
                                modifier = Modifier.size(90.dp),
                                backgroundColor = Color.Gray.copy(alpha = 0.2f),
                                color = MaterialTheme.colorScheme.primary,
                            )   */
                        }
                    }

                    PageState.Error -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Uh-oh🥲 \nSomething went wrong, click the button below to retry!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            IconButton(
                                onClick = { viewModel.getQuiz(topic ?: "German") },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier.size(62.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Refresh,
                                    contentDescription = "refresh",
                                )
                            }
                        }
                    }

                    PageState.Success -> {
                        val questions = uiState.questions ?: emptyList()
                        val pagerState = rememberPagerState(pageCount = { questions.size })
                        HorizontalPager(state = pagerState) { index ->
                            val question = questions[index]
                            QuizCard(question = question) {
                                coroutineScope.launch {
                                    if (index < questions.size - 1) {
                                        pagerState.animateScrollToPage(index + 1)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun QuizCard(question: QuizQuestion, scrollNext: () -> Unit = {}) {
    var selectedAnswer by rememberSaveable {
        mutableStateOf("")
    }

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(32.dp)) {
            Text(text = question.question, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                question.options.forEach { option ->

                    OutlinedCard(
                        onClick = { selectedAnswer = option }, modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                modifier = Modifier,
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(visible = selectedAnswer.isNotEmpty()) {
                OutlinedCard(
                    onClick = { /*TODO*/ }, colors = CardDefaults.cardColors(
                        containerColor = if (selectedAnswer.isCorrect(question.answer)) {
                            Color.Green.copy(alpha = .15f)
                        } else {
                            Color.Red.copy(alpha = .15f)
                        }
                    ), border = BorderStroke(
                        1.dp, if (selectedAnswer.isCorrect(question.answer)) {
                            Color.Green
                        } else {
                            Color.Red
                        }
                    ), modifier = Modifier.padding(top = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedAnswer.isCorrect(question.answer)) "The correct answer is: ${question.answer}" else "You got that wrong, the correct answer is: ${question.answer}",
                            modifier = Modifier.weight(1f),

                            )
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector = if (selectedAnswer.isCorrect(question.answer)) Icons.Rounded.CheckCircle else Icons.Rounded.Close,
                            contentDescription = "",
                            tint = if (selectedAnswer.isCorrect(question.answer)) Color.Green else Color.Red
                        )
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { scrollNext() }, modifier = Modifier.padding(top = 16.dp)) {
                    Text(text = "Next")
                }
            }
        }
    }
}