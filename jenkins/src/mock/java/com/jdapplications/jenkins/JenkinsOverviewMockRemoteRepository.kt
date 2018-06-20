package com.jdapplications.jenkins

import com.jdapplications.jenkins.data.Computer
import com.jdapplications.jenkins.data.JenkinsComputerResponse
import io.reactivex.Single

class JenkinsOverviewMockRemoteRepository : JenkinsOverviewRemoteRepository {
    override fun loadNodes(): Single<JenkinsComputerResponse> {
        val computer = Computer(displayName = "Slave 1", description = "First slave", offline = false)
        val computerTwo = Computer(displayName = "Slave 2", description = "Second slave", offline = true)
        val computerThree = Computer(displayName = "Slave 3", description = "Third slave", offline = false)
        val computerFour = Computer(displayName = "Slave 4", description = "Fourth slave", offline = false)
        val jenkinsComputerResponse = JenkinsComputerResponse(listOf(computer, computerTwo, computerThree, computerFour))
        return Single.just(jenkinsComputerResponse)
    }
}