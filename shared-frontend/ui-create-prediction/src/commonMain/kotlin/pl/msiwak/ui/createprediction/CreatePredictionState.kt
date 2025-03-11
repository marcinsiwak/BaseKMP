package pl.msiwak.ui.createprediction

import pl.msiwak.common.model.Player

data class CreatePredictionState(
    val players: List<Player> = emptyList()
)
