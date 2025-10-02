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
    sealed class ExampleDestination : NavDestination() {
        @Serializable
        data object Graph : ExampleDestination()

        @Serializable
        data object Screen : ExampleDestination()
    }

    @Serializable
    sealed class AiGeneratedDestination : NavDestination() {
        @Serializable
        data object Graph : AiGeneratedDestination()

        @Serializable
        data object Screen : AiGeneratedDestination()
    }

    @Serializable
    sealed class GameDestination : NavDestination() {
        @Serializable
        data object Graph : GameDestination()

        @Serializable
        data object StartScreen : GameDestination()

        @Serializable
        data object LobbyScreen : GameDestination()

        @Serializable
        data object CardsPreparationScreen : GameDestination()

        @Serializable
        data object RoundInfoScreen : GameDestination()

        @Serializable
        data object RoundScreen : GameDestination()

        @Serializable
        data object FinishScreen : GameDestination()
    }
}
