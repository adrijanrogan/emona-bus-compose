package com.rogandev.lpp.ui.screen

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.rogandev.lpp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}
