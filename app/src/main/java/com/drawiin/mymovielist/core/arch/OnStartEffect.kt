package com.drawiin.mymovielist.core.arch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * This composable is used to run a block of code when the composable is first composed.
 */
@Composable
fun OnStartSideEffect(block: () -> Unit) {
    var isFirstRecomposition by rememberSaveable { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (isFirstRecomposition) {
            block()
            isFirstRecomposition = false
        }
    }
}
