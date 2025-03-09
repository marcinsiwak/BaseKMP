package pl.msiwak.ui.createtype

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import fantasyleague.shared_frontend.ui_create_type.generated.resources.Res
import fantasyleague.shared_frontend.ui_create_type.generated.resources.allDrawableResources
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import pl.msiwak.common.model.Player

@Composable
fun CreateTypeScreen(viewModel: CreateTypeViewModel = koinInject()) {

    CreateTypeScreenContent(
        viewState = viewModel.viewState.collectAsState().value,
        onUiAction = viewModel::onUiAction
    )
}

@Composable
fun CreateTypeScreenContent(viewState: CreateTypeState, onUiAction: (CreateTypeUiAction) -> Unit) {
    Scaffold(
        content = {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .height(200.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    viewState.players.fastForEachIndexed { index, player ->
                        PlayerItem(
                            player = player,
                            onPlayerPicked = {
                                onUiAction(CreateTypeUiAction.PlayerPicked(index))
                            }
                        )
                    }
                }
            }
        }
    )
}


@Composable
private fun PlayerItem(player: Player, onPlayerPicked: (Player) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
    ) {
//        Icon(painter = painterResource(), contentDescription = null)
        Divider(
            color = Color.Black,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
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
