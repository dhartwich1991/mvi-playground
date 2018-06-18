package com.jdapplications.mvitest.ui.main

import com.jdapplications.mvitest.mvi.MviIntent

sealed class MainIntent : MviIntent {
    object InitialIntent : MainIntent()
    object ShowUsernameIntent : MainIntent()
    object LoadUsersIntent : MainIntent()
    data class SubmitFeedbackIntent(val feedback: String) : MainIntent()
}