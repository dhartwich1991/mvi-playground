package com.jdapplications.main.ui.main

import com.jdapplications.mvi_core.mvi.MviAction

sealed class MainAction : MviAction {
    data class SubmitFeedbackAction(val feedback: String) : MainAction()

    object ShowHelloWorldAction : MainAction()
    object ShowUsernameAction : MainAction()
    object LoadUsersAction : MainAction()
}