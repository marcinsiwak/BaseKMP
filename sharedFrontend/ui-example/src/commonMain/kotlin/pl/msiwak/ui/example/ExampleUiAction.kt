package pl.msiwak.ui.example

sealed class ExampleUiAction {
    data class PlayerPicked(val pos: Int) : ExampleUiAction()
    data object CreateExample : ExampleUiAction()
}
