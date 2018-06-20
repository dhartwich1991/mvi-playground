package com.jdapplications.jenkins

import com.jdapplications.mvi_core.mvi.MviIntent

sealed class JenkinsOverviewIntent : MviIntent {

    object InitialIntent : JenkinsOverviewIntent()

    object RefreshIntent : JenkinsOverviewIntent()
}
