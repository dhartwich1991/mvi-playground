package com.jdapplications.mvitest.ui.main

import com.jdapplications.mvitest.mvi.MviViewState

data class MainViewState(val username: String, val loading: Boolean, val feedbackSubmitted: Boolean, val error: Boolean,
                         val userList: List<String>) :
        MviViewState {
    companion object {
        fun initial(): MainViewState {
            return MainViewState("", false, false, false, emptyList())
        }
    }
}