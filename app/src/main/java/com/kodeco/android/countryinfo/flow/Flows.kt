package com.kodeco.android.countryinfo.flow

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
object Flows {
    private val _tapFlow = MutableStateFlow(0)
    val tapFlow = _tapFlow.asStateFlow()

    private val _backFlow = MutableStateFlow(0)
    val backFlow = _backFlow.asStateFlow()

    private val _counterFlow = MutableStateFlow(0)
    val counterFlow = _counterFlow.asStateFlow()

    // added refresh flow to track how many times screen was refreshed
    private val _refreshFlow = MutableStateFlow(0)
    val refreshFlow = _refreshFlow.asStateFlow()

    // added shared flow to show the toast every time when country row is tapped
    private val _countrySharedFlow = MutableSharedFlow<String>()
    val countrySharedFlow = _countrySharedFlow.asSharedFlow()

    // added shared flow to show the toast every time when refresh button is tapped
    private val _refreshSharedFlow = MutableSharedFlow<String>()
    val refreshSharedFlow = _refreshSharedFlow.asSharedFlow()

    // added combine flow variable, which combines tapFlow and backFlow
    val combineNavigationFlow = tapFlow.combine(backFlow) { tapFlow, backFlow ->
        tapFlow + backFlow
    }

    fun tap() {
        _tapFlow.value += 1
    }

    fun tapBack() {
        _backFlow.value += 1
    }

    fun tapRefresh() {
        _refreshFlow.value += 1
    }

    init {
        GlobalScope.launch {
            while (true) {
                delay(1_000L)
                _counterFlow.value += 1
            }
        }
    }

    fun triggerCountrySharedFlow(message: String) {
        GlobalScope.launch {
            _countrySharedFlow.emit(message)
        }
    }

    fun triggerRefreshSharedFlow(message: String) {
        GlobalScope.launch {
            _refreshSharedFlow.emit(message)
        }
    }
}
