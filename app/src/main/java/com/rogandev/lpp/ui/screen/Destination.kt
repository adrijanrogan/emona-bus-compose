package com.rogandev.lpp.ui.screen

sealed interface Destination {

    val route: String

    object Home : Destination {
        override val route: String
            get() = "home"
    }

    object Stations : Destination {
        override val route: String
            get() = "stations"
    }
}
