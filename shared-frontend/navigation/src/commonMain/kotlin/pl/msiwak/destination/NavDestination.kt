package pl.msiwak.destination

import kotlinx.serialization.Serializable

@Serializable
sealed class NavDestination {
    @Serializable
    sealed class ScreenADestination : NavDestination() {
        @Serializable
        data object Graph : ScreenADestination()

        @Serializable
        data object Screen : ScreenADestination()
    }

    @Serializable
    sealed class ScreenBDestination : NavDestination() {
        @Serializable
        data object Graph : ScreenBDestination()

        @Serializable
        data class Screen(val name: String = "") : ScreenBDestination()
    }

    @Serializable
    sealed class CreatePredictionDestination : NavDestination() {
        @Serializable
        data object Graph : CreatePredictionDestination()

        @Serializable
        data object Screen : CreatePredictionDestination()
    }
}
