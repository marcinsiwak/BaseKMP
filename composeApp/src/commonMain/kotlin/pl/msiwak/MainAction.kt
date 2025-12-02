package pl.msiwak

sealed class MainAction {
    data object OnDialogDismiss : MainAction()
    data object OnDialogConfirm : MainAction()

    data object OnLocalIPErrorDialogDismiss : MainAction()
    data object OnLocalIPErrorDialogConfirm : MainAction()
}
