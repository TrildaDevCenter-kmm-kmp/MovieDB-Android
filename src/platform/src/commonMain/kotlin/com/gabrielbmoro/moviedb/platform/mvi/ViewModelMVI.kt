package com.gabrielbmoro.moviedb.platform.mvi

import ModelViewIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class ViewModelMVI<in UserIntent : Any, ScreenState : Any> :
    ViewModel(),
    ModelViewIntent<UserIntent, ScreenState> {
    private val _uiState = MutableStateFlow(this.defaultEmptyState())
    val uiState = _uiState.stateIn(viewModelScope, SharingStarted.Eagerly, _uiState.value)

    fun accept(intent: UserIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            val state = execute(intent)
            _uiState.update { state }
        }
    }

    protected fun updateState(state: ScreenState) {
        _uiState.update { state }
    }

    protected fun getState(): ScreenState {
        return _uiState.value
    }
}
