package pl.msiwak.ui.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 120.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                onClick()
            }
            .border(1.dp, Color.Black, RoundedCornerShape(24.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = teamName,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
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
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
