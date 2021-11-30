package ru.ladgertha.testflowapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val oldFlow = MutableStateFlow(false)
    private val newFlow = Channel<Boolean>(capacity = CONFLATED)

    fun eventFromOldFlow() {
        oldFlow.value = true
    }

    fun eventFromNewFlow() {
        viewModelScope.launch {
            newFlow.send(true)
        }
    }

    internal fun getOldFLow(): Flow<Boolean> = oldFlow
    internal fun getNewFlow(): Flow<Boolean> = newFlow.receiveAsFlow()
}