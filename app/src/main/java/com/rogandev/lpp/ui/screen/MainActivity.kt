package com.rogandev.lpp.ui.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rogandev.lpp.ui.screen.home.HomeScreen
import com.rogandev.lpp.ui.screen.home.HomeViewModel
import com.rogandev.lpp.ui.screen.routes.RoutesScreen
import com.rogandev.lpp.ui.screen.routes.RoutesViewModel
import com.rogandev.lpp.ui.screen.station.StationScreen
import com.rogandev.lpp.ui.screen.station.StationViewModel
import com.rogandev.lpp.ui.screen.stations.StationsScreen
import com.rogandev.lpp.ui.screen.stations.StationsViewModel
import com.rogandev.lpp.ui.theme.EmonaTheme
import com.rogandev.lpp.ui.theme.Green
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmonaTheme {
                // System UI setup
                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Green,
                        darkIcons = false
                    )
                    systemUiController.setNavigationBarColor(
                        color = Color.White,
                        darkIcons = true
                    )
                }

                // Navigation setup
                val navController = rememberAnimatedNavController()
                
                AnimatedNavHost(
                    navController = navController,
                    startDestination = Navigation.Home.route,
                    enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) } ,
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
                ) {
                    composable(Navigation.Home.route) {
                        val viewModel = hiltViewModel<HomeViewModel>()
                        HomeScreen(viewModel = viewModel, navController = navController)
                    }

                    composable(Navigation.Stations.route) {
                        val viewModel = hiltViewModel<StationsViewModel>()
                        val state by viewModel.uiStateFlow.collectAsState()
                        StationsScreen(
                            state = state,
                            onBackClick = {
                                navController.navigateUp()
                            },
                            onStationClick = { station ->
                                navController.navigate(Navigation.Station.build(station.id))
                            }
                        )
                    }

                    composable(Navigation.Station.route, arguments = Navigation.Station.arguments) { backStackEntry ->
                        val id = Navigation.Station.getIdArgument(backStackEntry)
                        val viewModel = hiltViewModel<StationViewModel>()
                        viewModel.setStationCode(id)
                        val state by viewModel.uiStateFlow.collectAsState()

                        StationScreen(
                            state = state,
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )
                    }

                    composable(Navigation.Routes.route) {
                        val viewModel = hiltViewModel<RoutesViewModel>()
                        val state by viewModel.uiStateFlow.collectAsState()
                        RoutesScreen(
                            state = state,
                            onBackClick = { navController.navigateUp() },
                            onRouteClick = { }
                        )
                    }
                }
            }
        }
    }
}
