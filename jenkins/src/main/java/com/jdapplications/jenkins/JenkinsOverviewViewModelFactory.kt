package com.jdapplications.jenkins

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jdapplications.core.SingletonHolderSingleArg

class JenkinsOverviewViewModelFactory private constructor(private val applicationContext: Context)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == JenkinsOverviewViewModel::class.java) {
            return JenkinsOverviewViewModel(
                    JenkinsOverviewActionProcessorHolder(JenkinsInjections.provideJenkinsRemoteRepository())
            ) as T
        }
        throw IllegalArgumentException("unknown model class " + modelClass)
    }

    companion object : SingletonHolderSingleArg<JenkinsOverviewViewModelFactory, Context>(::JenkinsOverviewViewModelFactory)
}