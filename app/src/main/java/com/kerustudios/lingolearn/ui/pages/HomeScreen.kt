package com.kerustudios.lingolearn.ui.pages

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kerustudios.lingolearn.ui.navigation.PracticePage

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, navController: NavHostController
) {
    Scaffold(modifier = Modifier) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "LingoLearn",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(top = 54.dp)
                    .padding(horizontal = 16.dp),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 48.sp
            )

            HomeCard(
                title = "practice",
                subtitle = "learn new words and phrases",
                modifier = Modifier.padding(top = 16.dp),
                onClick = { topic ->
                    navController.navigate(PracticePage(topic))
                }
            )

        }
    }

}

@Composable
fun HomeCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onClick: (String) -> Unit
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(true)
    }

    var topic by rememberSaveable {
        mutableStateOf("")
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isExpanded) Color.White else MaterialTheme.colorScheme.surface,
        label = "cardBackgroundColor"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isExpanded) Color.Black else MaterialTheme.colorScheme.onSurface.copy(
            alpha = .5f
        ),
        label = "cardContentColor"
    )

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = { isExpanded = !isExpanded },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor, contentColor = contentColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = title, fontSize = 36.sp, modifier = Modifier)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = subtitle, modifier = Modifier)
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(visible = isExpanded) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = topic,
                            onValueChange = { topic = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Enter a topic to practice ...") },
                            shape = RoundedCornerShape(16.dp),
                        )

                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .clickable {
                                    onClick(topic)

                                }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = "",
                                modifier = Modifier.padding(18.dp)
                            )
                        }
                    }
                }
            }
            Icon(
                imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                contentDescription = "",
            )

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

