package com.rogandev.lpp.ui.screen.routes

import com.rogandev.lpp.ui.model.UiRoute
import com.rogandev.lpp.ui.model.UiRouteGroup

object RouteComparator : Comparator<UiRoute> {

    override fun compare(first: UiRoute, second: UiRoute): Int {
        return RouteGroupComparator.compare(first.routeGroup, second.routeGroup)
    }
}

object RouteGroupComparator : Comparator<UiRouteGroup> {

    override fun compare(first: UiRouteGroup, second: UiRouteGroup): Int {
        val digitsFirst = first.name.count { it.isDigit() }
        val digitsSecond = second.name.count { it.isDigit() }

        if (digitsFirst != digitsSecond) {
            return digitsFirst - digitsSecond
        }

        return first.name.compareTo(second.name)
    }
}
