package com.jdapplications.jenkins.data

import com.squareup.moshi.Json

data class Computer(@Json(name = "description")
                    val description: String? = "",
                    @Json(name = "displayName")
                    val displayName: String? = "",
                    @Json(name = "icon")
                    val icon: String? = "",
                    @Json(name = "iconClassName")
                    val iconClassName: String? = "",
                    @Json(name = "idle")
                    val idle: Boolean? = false,
                    @Json(name = "numExecutors")
                    val numExecutors: Int? = 0,
                    @Json(name = "offline")
                    val offline: Boolean = false,
//                    @Json(name = "offlineCause")
//                    val offlineCause: String? = "",
                    @Json(name = "offlineCauseReason")
                    val offlineCauseReason: String? = "",
                    @Json(name = "temporarilyOffline")
                    val temporarilyOffline: Boolean = false)