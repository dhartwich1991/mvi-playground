package com.jdapplications.mvitest.main.ui.main

import com.jdapplications.mvi_core.mvi.MviAction

sealed class MainAction : MviAction {
    object InitialAction : MainAction()
    object UpdateStatusAction : MainAction()
}