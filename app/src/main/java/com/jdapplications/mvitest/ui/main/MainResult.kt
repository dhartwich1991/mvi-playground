package com.jdapplications.mvitest.ui.main

import com.jdapplications.mvitest.mvi.MviResult

sealed class MainResult : MviResult {
    sealed class LoadWelcomeTextResults : MainResult() {
        data class Success(val username: String) : LoadWelcomeTextResults()
        object Failure : LoadWelcomeTextResults()
    }

    sealed class SubmitFeedbackResults : MainResult() {
        object Submitting : SubmitFeedbackResults()
        object Success : SubmitFeedbackResults()
        object Failure : SubmitFeedbackResults()
    }

    sealed class LoadUserListResults : MainResult() {
        data class Success(val list: List<String>) : LoadUserListResults()
        object Failure : LoadUserListResults()
        object Loading : LoadUserListResults()
    }
}