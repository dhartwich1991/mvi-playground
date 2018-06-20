package com.jdapplications.jenkins

import com.jdapplications.jenkins.data.JenkinsComputerResponse
import io.reactivex.Single
import retrofit2.http.GET


interface JenkinsOverviewRemoteRepository {
    @GET("computer/api/json")
    fun loadNodes(): Single<JenkinsComputerResponse>
}
