package pl.msiwak.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

val customEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) =
    {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(500)
        ) + fadeIn(animationSpec = tween(500))
    }

val customExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) =
    {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(500)
        ) + fadeOut(animationSpec = tween(500))
    }
