package com.rogandev.lpp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rogandev.lpp.R
import com.rogandev.lpp.ui.component.StationList
import com.rogandev.lpp.ui.theme.EmonaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<StationsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EmonaTheme {
                MainScaffold(viewModel)
            }
        }
    }
}

@Composable
fun MainScaffold(viewModel: StationsViewModel) {

    val state by viewModel.uiStateFlow.collectAsState()

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp),
                color = MaterialTheme.colors.background,
                elevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "All stops", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        },

        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                StationList(stations = state.stations)

                if (state.loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        },

        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 60.dp),
                color = MaterialTheme.colors.background,
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 60.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Surface(shape = CircleShape, modifier = Modifier.size(50.dp)) {
                        Image(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(onClick = {}),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_bus),
                            contentDescription = "Bus",
                            contentScale = ContentScale.Inside,
                        )
                    }

                    Surface(shape = CircleShape, modifier = Modifier.size(50.dp)) {
                        Image(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(onClick = {}),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_trip),
                            contentDescription = "Trip",
                            contentScale = ContentScale.Inside,
                        )
                    }

                    Surface(shape = CircleShape, modifier = Modifier.size(50.dp)) {
                        Image(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(onClick = {}),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_book),
                            contentDescription = "Bus",
                            contentScale = ContentScale.Inside,
                        )
                    }
                }
            }
        }
    )
}
