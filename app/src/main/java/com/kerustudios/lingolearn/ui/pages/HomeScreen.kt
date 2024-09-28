package com.kerustudios.lingolearn.ui.pages

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kerustudios.lingolearn.ui.navigation.PracticePage


@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "LingoLearn",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(top = 54.dp)
                .padding(horizontal = 16.dp),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 48.sp,
        )

        HomeCard(title = "practice",
            subtitle = "practice new words and improve your vocabulary",
            modifier = Modifier.padding(top = 16.dp),
            onClick = { language ->
                if (language.isNotEmpty())
                    navController.navigate(PracticePage(language))
                else Toast.makeText(context, "Please, enter a language!", Toast.LENGTH_SHORT).show()
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCard(
    modifier: Modifier = Modifier, title: String, subtitle: String, onClick: (String) -> Unit
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var topic by rememberSaveable {
        mutableStateOf("")
    }
    var isDropDownExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    var languageLevel by rememberSaveable {
        mutableStateOf("Beginner")
    }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = { isExpanded = !isExpanded },
        shape = RoundedCornerShape(32.dp),
    ) {
        Column(modifier = Modifier.padding(32.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, fontSize = 36.sp, modifier = Modifier)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = subtitle, modifier = Modifier)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "",
                )

            }
            AnimatedVisibility(
                visible = isExpanded, modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        placeholder = { Text("Enter language to practice ...") },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    ExposedDropdownMenuBox(
                        expanded = isDropDownExpanded, onExpandedChange = {
                            isDropDownExpanded = !isDropDownExpanded
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        TextField(
                            value = languageLevel,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropDownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = isDropDownExpanded,
                            onDismissRequest = { isDropDownExpanded = !isDropDownExpanded },
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            DropdownMenuItem(text = {
                                Text(text = "Beginner")
                            }, onClick = {
                                languageLevel = "Beginner"
                                isDropDownExpanded = !isDropDownExpanded
                            })
                            DropdownMenuItem(text = {
                                Text(text = "Intermediate")
                            }, onClick = {
                                languageLevel = "Intermediate"
                                isDropDownExpanded = !isDropDownExpanded
                            })
                            DropdownMenuItem(text = {
                                Text(text = "Advanced")
                            }, onClick = {
                                languageLevel = "Advanced"
                                isDropDownExpanded = !isDropDownExpanded
                            })
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        onClick = { onClick("$languageLevel $topic") }
                    ) {
                        Text(text = "Start Practice")
                    }

                }
            }

        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=1080px,height=2340px,dpi=440,isRound=true",
    backgroundColor = 0xFF090909
)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}

