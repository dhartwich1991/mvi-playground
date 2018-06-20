package com.jdapplications.jenkins

import com.jdapplications.jenkins.data.Computer
import com.jdapplications.mvi_core.mvi.MviViewState

data class JenkinsOverviewViewState(val loading: Boolean, val computers: List<Computer>, val error: Throwable?) : MviViewState {
    companion object {
        val INITIAL = JenkinsOverviewViewState(false, emptyList(), null)
    }
}
