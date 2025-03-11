package pl.msiwak.ui.createprediction

sealed class CreatePredictionUiAction {
    data class PlayerPicked(val pos: Int) : CreatePredictionUiAction()
    data object CreatePrediction : CreatePredictionUiAction()
}
