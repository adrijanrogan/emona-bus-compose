package com.rogandev.lpp.ui.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface Navigation {

    val route: String

    val arguments: List<NamedNavArgument>
        get() = emptyList()

    object Home : Navigation {
        override val route: String
            get() = "home"
    }

    object Stations : Navigation {
        override val route: String
            get() = "stations"
    }

    object Station : Navigation {

        override val route: String
            get() = "station/{id}"

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )

        fun build(id: String) = "station/$id"

        fun getIdArgument(backStackEntry: NavBackStackEntry): String =
            backStackEntry.arguments?.getString("id")
                ?: throw IllegalStateException("Station route requires ID argument")
    }

    object Routes : Navigation {
        override val route: String
            get() = "routes"
    }
}
