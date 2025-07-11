package pl.msiwak.ui.example

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import basekmp.shared_frontend.common_resources.generated.resources.Res
import basekmp.shared_frontend.common_resources.generated.resources.ic_check
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import pl.msiwak.common.model.Player

@Composable
fun ExampleScreen(viewModel: ExampleViewModel = koinInject()) {
    ExampleScreenContent(
        viewState = viewModel.viewState.collectAsState().value,
        onUiAction = viewModel::onUiAction
    )
}

@Composable
fun ExampleScreenContent(viewState: ExampleState, onUiAction: (ExampleUiAction) -> Unit) {
    Scaffold(
        content = {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .height(260.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    viewState.players.fastForEachIndexed { index, player ->
                        PlayerItem(
                            player = player,
                            onPlayerPicked = {
                                onUiAction(ExampleUiAction.PlayerPicked(index))
                            }
                        )
                    }
                }
                Button(
                    onClick = {
                        onUiAction(ExampleUiAction.CreateExample)
                    }
                ) {
                    Text("Create")
                }
            }
        }
    )
}

@Composable
private fun PlayerItem(player: Player, onPlayerPicked: (Player) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable {
                onPlayerPicked(player)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (player.isPicked) {
            Icon(
                modifier = Modifier.width(64.dp),
                painter = painterResource(Res.drawable.ic_check),
                contentDescription = null
            )
        } else {
            Spacer(modifier = Modifier.width(64.dp))
        }
        Text(
            modifier = Modifier.width(64.dp).padding(8.dp),
            text = player.number
        )
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            modifier = Modifier.width(156.dp).padding(8.dp),
            text = player.name + player.surname
        )
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            modifier = Modifier.width(64.dp).padding(8.dp),
            text = player.points
        )
    }
}
