package pl.msiwak.basekmp

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import pl.msiwak.basekmp.navigation.AppNavHost

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        AppNavHost(navController)
    }
}