package pl.msiwak.navigator

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import pl.msiwak.destination.NavDestination

class Navigator {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    suspend fun navigate(
        navDestination: NavDestination
    ) {
        _navigationEvent.emit(NavigationEvent.NavigateToDestination(navDestination))
    }

    suspend fun navigateUp() {
        _navigationEvent.emit(NavigationEvent.NavigateUp)
    }
}