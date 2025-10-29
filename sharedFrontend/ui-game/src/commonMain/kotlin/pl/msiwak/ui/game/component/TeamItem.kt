package pl.msiwak.ui.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.msiwak.common.model.Player

@Composable
fun TeamItemComponent(
    modifier: Modifier = Modifier,
    teamName: String,
    players: List<Player>,
    onClick: (() -> Unit) = {}
) {
    CustomBackground(
        modifier = modifier
    ) {
        Box(
            Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 140.dp)
                    .padding(vertical = 12.dp)
                    .clip(RoundedCornerShape(42.dp))
                    .clickable { onClick() }
                    .padding(vertical = 16.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
                ) {
                    players.forEachIndexed { index, player ->
                        TeamPlayerItemComponent(
                            playerName = player.name,
//                    avatarIcon = avatarIcon,
                            backgroundColor = Color.Transparent
                        )
                    }

                }
            }
            Text(
                modifier = Modifier
                    .padding(start = 4.dp, top = 4.dp)
                    .background(Color.Black, shape = CircleShape)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.TopStart),
                text = teamName,
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun TeamPlayerItemComponent(
    playerName: String,
    avatarIcon: ImageVector? = null,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
//        Image(
//            imageVector = avatarIcon,
//            contentDescription = "$playerName's Avatar",
//            modifier = Modifier.size(32.dp),
//            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
//        )

        Text(
            text = playerName,
            color = Color.Black,
            fontSize = 12.sp
        )
    }
}
