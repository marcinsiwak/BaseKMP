package pl.msiwak.navigator

import pl.msiwak.destination.NavDestination

sealed class NavigationEvent {

    data class NavigateToDestination(
        val navDestination: NavDestination
    ) : NavigationEvent()

    data object NavigateUp : NavigationEvent()
}
