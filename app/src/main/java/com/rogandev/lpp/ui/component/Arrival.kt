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
import com.rogandev.lpp.ui.model.UiArrivalGroup
import com.rogandev.lpp.ui.model.UiRouteGroup
import kotlin.random.Random

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArrivalCard(modifier: Modifier = Modifier, arrivalGroup: UiArrivalGroup, onArrivalClick: (UiArrivalGroup) -> Unit) {
    Surface(
        modifier = modifier,
        elevation = 2.dp,
        shape = RoundedCornerShape(10.dp),
        content = {
            ArrivalContent(
                modifier = Modifier.padding(10.dp),
                arrivalGroup = arrivalGroup
            )
        },
        onClick = {
            onArrivalClick(arrivalGroup)
        }
    )
}

@Composable
fun ArrivalContent(modifier: Modifier = Modifier, arrivalGroup: UiArrivalGroup) {

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        // Route indicator on the left
        RouteIndicator(size = 50.dp, routeGroup = arrivalGroup.routeGroup)

        Spacer(modifier = Modifier.width(20.dp))

        // Route directions
        Column {
            Text(text = arrivalGroup.destination, fontWeight = FontWeight.Normal, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier.horizontalScroll(ScrollState(0)),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
                verticalAlignment = Alignment.Bottom,
            ) {
                arrivalGroup.arrivals.forEachIndexed { index, arrival ->
                    if (index == 0) {
                        Text(text = arrival.delta, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    } else {
                        Text(text = arrival.delta, fontWeight = FontWeight.Normal, fontSize = 16.sp)
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
        UiArrivalGroup(
            routeGroup = UiRouteGroup.fromName("7"),
            destination = "Črnuče",
            arrivals = randomArrivals(3)
        ),
        UiArrivalGroup(
            routeGroup = UiRouteGroup.fromName("13"),
            destination = "Center Stožice P+R",
            arrivals = randomArrivals(1),
        ),
        UiArrivalGroup(
            routeGroup = UiRouteGroup.fromName("14"),
            destination = "Bokalce",
            arrivals = randomArrivals(6),
        ),
    )

    Column(modifier = Modifier.width(400.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        arrivals.forEach { ArrivalCard(
            modifier = Modifier.fillMaxWidth(),
            arrivalGroup = it, onArrivalClick = {}
        ) }
    }
}

private fun randomArrivals(count: Int) = (1..count).map {
    Random.nextInt(0, 60)
}.sorted().map { delta ->
    val deltaText = if (delta == 0) "Prihod" else "$delta min"
    UiArrival(deltaText, Random.nextBoolean())
}
