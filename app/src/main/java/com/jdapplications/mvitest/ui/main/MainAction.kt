package com.jdapplications.mvitest.ui.main

import com.jdapplications.mvitest.mvi.MviAction

sealed class MainAction : MviAction {
    data class SubmitFeedbackAction(val feedback: String) : MainAction()

    object ShowHelloWorldAction : MainAction()
    object ShowUsernameAction : MainAction()
    object LoadUsersAction : MainAction()
}