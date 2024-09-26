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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kerustudios.lingolearn.data.models.QuizQuestion
import com.kerustudios.lingolearn.utils.isCorrect
import kotlinx.coroutines.launch

@Composable
fun PracticeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    topic: String?,
    viewModel: PracticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "")
            }
            Text(
                text = "practice", fontSize = 12.sp, modifier = Modifier
                    .alpha(.6f)
                    .weight(1f)
            )
            IconButton(onClick = { }) {}
        }

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
        ) {
            uiState.quiz?.let { quiz ->
                val pagerState = rememberPagerState(pageCount = { quiz.quiz.size })
                HorizontalPager(state = pagerState) {
                    val question = quiz.quiz[it]
                    QuizCard(question = question) {
                        coroutineScope.launch {
                            if (it < quiz.quiz.size - 1) {
                                pagerState.animateScrollToPage(it + 1)
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
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
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