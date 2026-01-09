package pl.msiwak

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.rememberNavController
import cardsthegame.sharedfrontend.common_resources.generated.resources.Res
import cardsthegame.sharedfrontend.common_resources.generated.resources.done
import cardsthegame.sharedfrontend.common_resources.generated.resources.img_background
import cardsthegame.sharedfrontend.common_resources.generated.resources.local_ip_error
import cardsthegame.sharedfrontend.common_resources.generated.resources.try_again
import cardsthegame.sharedfrontend.common_resources.generated.resources.turn_on_wifi
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import pl.msiwak.cardsthegame.common.resources.GameLightColorScheme
import pl.msiwak.cardsthegame.common.resources.MyTypography
import pl.msiwak.globalloadermanager.component.GlobalLoader
import pl.msiwak.navigation.AppNavHost
import pl.msiwak.navigator.NavigationEvent

@Composable
@Preview
fun App(
    viewModel: MainViewModel2 = koinInject()
) {
    val viewState = viewModel.viewState.collectAsState()

    val navController = rememberNavController()
    LaunchedEffect(key1 = Unit) {
        viewModel.navigator.navigationEvent.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateToDestination -> navController.navigate(event.navDestination)
                NavigationEvent.NavigateUp -> navController.navigateUp()
            }
        }
    }
    MaterialTheme(
        colors = GameLightColorScheme,
        typography = MyTypography()
    ) {
        Box(
            modifier = Modifier.fillMaxSize().imePadding()
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.img_background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            AppNavHost(navController)

            if (viewState.value.isLoading) {
                GlobalLoader(
                    modifier = Modifier.align(Center),
                    message = viewState.value.loaderMessage
                )
            }
            if (viewState.value.isWifiDialogVisible) {
                Dialog(
                    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
                    onDismissRequest = { viewModel.onUIAction(MainAction.OnDialogDismiss) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.turn_on_wifi),
                        )
                        Button(
                            onClick = {
                                viewModel.onUIAction(MainAction.OnDialogConfirm)
                            }
                        ) {
                            Text(text = stringResource(Res.string.done))
                        }
                    }
                }
            }
            if (viewState.value.localIpIssueError) {
                Dialog(
                    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
                    onDismissRequest = { viewModel.onUIAction(MainAction.OnLocalIPErrorDialogDismiss) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(Res.string.local_ip_error),
                        )
                        Button(
                            onClick = {
                                viewModel.onUIAction(MainAction.OnLocalIPErrorDialogConfirm)
                            }
                        ) {
                            Text(text = stringResource(Res.string.try_again))
                        }
                    }
                }
            }
        }
    }
}
