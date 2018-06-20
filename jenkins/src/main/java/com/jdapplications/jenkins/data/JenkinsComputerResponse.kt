package com.jdapplications.jenkins.data

import com.squareup.moshi.Json

data class JenkinsComputerResponse(@field:Json(name = "computer") val computers: List<Computer>)