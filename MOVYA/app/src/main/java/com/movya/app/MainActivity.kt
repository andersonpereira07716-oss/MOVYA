package com.movya.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.movya.app.ui.navigation.NavGraph
import com.movya.app.ui.theme.MOVYATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MOVYATheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
