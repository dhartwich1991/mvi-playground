package com.jdapplications.jenkins

import com.jdapplications.mvi_core.mvi.MviAction

sealed class JenkinsOverviewAction : MviAction {
    object LoadJenkinsOverviewAction : JenkinsOverviewAction()
}