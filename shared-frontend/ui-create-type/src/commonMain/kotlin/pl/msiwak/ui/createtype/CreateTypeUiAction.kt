package pl.msiwak.ui.createtype

sealed class CreateTypeUiAction {
    data class PlayerPicked(val pos: Int) : CreateTypeUiAction()
}
