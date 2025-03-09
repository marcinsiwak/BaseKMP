package pl.msiwak.ui.createprediction

sealed class CreateTypeUiAction {
    data class PlayerPicked(val pos: Int) : CreateTypeUiAction()
    data object CreatePrediction : CreateTypeUiAction()
}
