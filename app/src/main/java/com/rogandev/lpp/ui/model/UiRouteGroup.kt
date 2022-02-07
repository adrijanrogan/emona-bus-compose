package com.rogandev.lpp.ui.model

import androidx.compose.ui.graphics.Color

data class UiRouteGroup(val name: String, val color: Color) {

    companion object {
        fun fromName(name: String): UiRouteGroup {

            val color = when (name.trimStart { it.isDigit().not() || it == '0' }) {
                "1" -> Color(0xFFca3736)
                "1B" -> Color(0xFFca3736)
                "1D" -> Color(0xFFca3736)
                "2" -> Color(0xFF8c8741)
                "3" -> Color(0xFFec5a3a)
                "3B" -> Color(0xFFec5a3a)
                "3G" -> Color(0xFFec5a3a)
                "5" -> Color(0xFF9f549d)
                "6" -> Color(0xFF939598)
                "6B" -> Color(0xFFb0b0b1)
                "7" -> Color(0xFF0eb9db)
                "7L" -> Color(0xFF0eb9db)
                "8" -> Color(0xFF036aaf)
                "9" -> Color(0xFF88aacd)
                "11" -> Color(0xFFecc23b)
                "11B" -> Color(0xFFecc23b)
                "12" -> Color(0xFF284ba0)
                "12D" -> Color(0xFF849daa)
                "13" -> Color(0xFFd0d34d)
                "14" -> Color(0xFFef5ba1)
                "15" -> Color(0xFFa3238e)
                "18" -> Color(0xFF885835)
                "18L" -> Color(0xFF885835)
                "19B" -> Color(0xFFe89db4)
                "19I" -> Color(0xFFe89db4)
                else -> Color(0xFFb0b0b0)
            }

            return UiRouteGroup(name, color)
        }
    }
}
