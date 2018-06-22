package com.jdapplications.mvitest.main.ui.main

import com.jdapplications.mvi_core.mvi.MviResult

sealed class MainResult : MviResult {
    sealed class UpdateStatusResults : MainResult() {
        object Success : UpdateStatusResults()
        object Failure : UpdateStatusResults()
        object Loading : UpdateStatusResults()
    }

    object InitialResult : MainResult()
}