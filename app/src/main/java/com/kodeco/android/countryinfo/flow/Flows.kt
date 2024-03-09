package com.kodeco.android.countryinfo.flow

import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
object Flows {
    private val _tapFlow = MutableStateFlow(0)
    val tapFlow = _tapFlow.asStateFlow()

    private val _backFlow = MutableStateFlow(0)
    val backFlow = _backFlow.asStateFlow()

    private val _counterFlow = MutableStateFlow(0)
    val counterFlow = _counterFlow.asStateFlow()

    fun tap() {
        _tapFlow.value += 1
    }

    fun tapBack() {
        _backFlow.value += 1
    }
}
