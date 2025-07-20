package com.turkoglu.moviecomposeapp.presentation.settings.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.presentation.login.AuthViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            val id = painterResource(id = R.drawable.user)
            Image(painter = id, contentDescription = null, modifier = Modifier.size(200.dp))

            Text(
                text = "Sign Out",
                color = Color.Red,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .clickable {
                        viewModel.clearCredentials()
                        navController.navigate("Login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
            )
        }
    }
}