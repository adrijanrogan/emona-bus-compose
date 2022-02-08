package com.rogandev.lpp.ui.screen.routes

import com.rogandev.lpp.ui.model.UiRoute
import com.rogandev.lpp.ui.model.UiRouteGroup

object RouteComparator : Comparator<UiRoute> {

    override fun compare(first: UiRoute, second: UiRoute): Int {
        return RouteGroupComparator.compare(first.routeGroup, second.routeGroup)
    }
}

object RouteGroupComparator : Comparator<UiRouteGroup> {

    // Compares route groups, loosely based on regex [\D]*[\d]*[\D]*

    override fun compare(first: UiRouteGroup, second: UiRouteGroup): Int {
        val prefixFirst = first.name.takeWhile { it.isDigit().not() }
        val prefixSecond = second.name.takeWhile { it.isDigit().not() }
        val prefixCmp = prefixFirst.compareTo(prefixSecond)
        if (prefixCmp != 0)
            return prefixCmp

        val digitsFirst = first.name.filter { it.isDigit() }
        val digitsSecond = second.name.filter { it.isDigit() }

        val digitsDiff = digitsFirst.length - digitsSecond.length
        if (digitsDiff != 0)
            return digitsDiff

        val digitsCmp = digitsFirst.compareTo(digitsSecond)
        if (digitsCmp != 0)
            return digitsCmp

        val suffixFirst = first.name.takeLastWhile { it.isDigit().not() }
        val suffixSecond = second.name.takeLastWhile { it.isDigit().not() }
        return suffixFirst.compareTo(suffixSecond)
    }
}
