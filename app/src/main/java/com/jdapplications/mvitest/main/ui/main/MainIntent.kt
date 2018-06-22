package com.jdapplications.mvitest.main.ui.main

import com.jdapplications.mvi_core.mvi.MviIntent

sealed class MainIntent : MviIntent {
    object InitialIntent : MainIntent()
    object UpdateStatusIntent : MainIntent()

}