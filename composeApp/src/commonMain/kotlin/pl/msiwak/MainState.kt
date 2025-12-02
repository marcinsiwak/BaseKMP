package pl.msiwak

data class MainState(
    val isLoading: Boolean = false,
    val loaderMessage: String? = null,
    val isWifiDialogVisible: Boolean = false,
    val localIpIssueError: Boolean = false
)
