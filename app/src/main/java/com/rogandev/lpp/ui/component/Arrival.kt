package com.rogandev.lpp.ui.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rogandev.lpp.ui.model.UiArrival
import com.rogandev.lpp.ui.model.UiRouteGroup

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArrivalCard(modifier: Modifier = Modifier, arrival: UiArrival, onArrivalClick: (UiArrival) -> Unit) {
    Surface(
        modifier = modifier,
        elevation = 2.dp,
        shape = RoundedCornerShape(10.dp),
        content = {
            ArrivalContent(
                modifier = Modifier.padding(10.dp),
                arrival = arrival
            )
        },
        onClick = {
            onArrivalClick(arrival)
        }
    )
}

@Composable
fun ArrivalContent(modifier: Modifier = Modifier, arrival: UiArrival) {

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        // Route indicator on the left
        RouteIndicator(size = 50.dp, routeGroup = arrival.routeGroup)

        Spacer(modifier = Modifier.width(20.dp))

        // Route directions
        Column {
            Text(text = arrival.destination, fontWeight = FontWeight.Normal, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier.horizontalScroll(ScrollState(0)),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
                verticalAlignment = Alignment.Bottom,
            ) {
                arrival.etas.forEachIndexed { index, eta ->
                    if (index == 0) {
                        Text(text = eta, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    } else {
                        Text(text = eta, fontWeight = FontWeight.Normal, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ArrivalPreview() {
    val arrivals = listOf(
        UiArrival(
            routeGroup = UiRouteGroup.fromName("7"),
            destination = "Črnuče",
            etas = listOf("25 min", "48 min")
        ),
        UiArrival(
            routeGroup = UiRouteGroup.fromName("13"),
            destination = "Center Stožice P+R",
            etas = listOf("6 min")
        ),
        UiArrival(
            routeGroup = UiRouteGroup.fromName("14"),
            destination = "Bokalce",
            etas = listOf("7 min ᴳ", "10 min", "20 min", "30 min", "42 min", "59 min")
        ),
    )

    Column(modifier = Modifier.width(400.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        arrivals.forEach { ArrivalCard(
            modifier = Modifier.fillMaxWidth(),
            arrival = it, onArrivalClick = {}
        ) }
    }
}
