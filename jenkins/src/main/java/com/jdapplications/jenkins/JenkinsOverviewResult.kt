package com.jdapplications.jenkins

import com.jdapplications.jenkins.data.Computer
import com.jdapplications.mvi_core.mvi.MviResult

sealed class JenkinsOverviewResult : MviResult {
    sealed class LoadJenkinsOverviewResult : JenkinsOverviewResult() {
        object Loading : LoadJenkinsOverviewResult()
        data class Success(val computers: List<Computer>) : LoadJenkinsOverviewResult()
        data class Failure(val error: Throwable
        ) : LoadJenkinsOverviewResult()

    }
}