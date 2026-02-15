package pl.msiwak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.msiwak.basekmp.remoteconfig.RemoteConfig
import pl.msiwak.globalloadermanager.GlobalLoaderManager
import pl.msiwak.globalloadermanager.GlobalLoaderMessageType
import pl.msiwak.navigator.Navigator

class MainViewModel(
    private val remoteConfig: RemoteConfig,
    val navigator: Navigator,
    private val globalLoaderManager: GlobalLoaderManager
) : ViewModel() {

    private val _viewState = MutableStateFlow(MainState())
    val viewState: StateFlow<MainState> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                globalLoaderManager.globalLoaderState.filterNotNull().collectLatest { loaderState ->
                    _viewState.update {
                        it.copy(
                            isLoading = loaderState.isLoading
                        )
                    }
                }
            }
        }
    }

    fun onUIAction(action: MainAction) {

    }
}
