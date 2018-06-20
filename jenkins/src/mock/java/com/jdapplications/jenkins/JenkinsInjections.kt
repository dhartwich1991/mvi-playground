package com.jdapplications.jenkins

object JenkinsInjections {
    fun provideJenkinsRemoteRepository(): JenkinsOverviewRemoteRepository {
        //Return instance of Retrofit with the jenkins service.
        return JenkinsOverviewMockRemoteRepository()
    }
}
