package com.kerustudios.lingolearn.ui.pages

import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kerustudios.lingolearn.R
import com.kerustudios.lingolearn.ui.navigation.HomePage
import com.kerustudios.lingolearn.ui.theme.AppTheme

@Composable
fun AuthScreen(
    navController: NavHostController,
    vm: AuthPageViewModel = hiltViewModel()
) {
    val startAccountIntentLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            vm.handleSignIn(null)
        }

    val uiState by vm.uiState.collectAsState()
    val context = LocalContext.current

    when (uiState.authStatus) {
        is AuthStatus.Success -> {
            navController.navigate(HomePage)
        }

        is AuthStatus.Error -> {
            Toast.makeText(context, (uiState.authStatus as AuthStatus.Error).message, Toast.LENGTH_SHORT).show()
        }
        AuthStatus.None -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.an_elephant_language),
            contentDescription = "",
            modifier = Modifier.size(240.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                vm.handleSignIn {
                    startAccountIntentLauncher.launch(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Continue with Google")

        }
        Text(
            text = "By, continuing, you agree to LingoLearn's terms & conditions.",
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            modifier = Modifier
                .alpha(.65f)
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        )


    }
}

@Preview(
    showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun AuthScreenPreview() {
    AppTheme {
        AuthScreen(navController = rememberNavController())
    }
}