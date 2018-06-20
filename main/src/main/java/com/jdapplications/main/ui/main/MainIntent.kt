package com.jdapplications.main.ui.main

import com.jdapplications.mvi_core.mvi.MviIntent

sealed class MainIntent : MviIntent {
    object InitialIntent : MainIntent()
    object ShowUsernameIntent : MainIntent()
    object LoadUsersIntent : MainIntent()
    data class SubmitFeedbackIntent(val feedback: String) : MainIntent()
}