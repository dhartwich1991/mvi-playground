package com.jdapplications.jenkins

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class JenkinsOverviewActionProcessorHolder(private val jenkinsOverviewRemoteRepository: JenkinsOverviewRemoteRepository) {
    internal val actionProcessor = ObservableTransformer<JenkinsOverviewAction, JenkinsOverviewResult> { actions ->
        actions.publish { shared ->
            Observable.merge<JenkinsOverviewResult>(
                    listOf(shared.ofType(JenkinsOverviewAction.LoadJenkinsOverviewAction::class.java).compose
                    (loadJenkinsOverviewProcessor))
            )
        }
    }

    private val loadJenkinsOverviewProcessor = ObservableTransformer<JenkinsOverviewAction.LoadJenkinsOverviewAction,
            JenkinsOverviewResult.LoadJenkinsOverviewResult> { actions ->
        actions.flatMap { action ->
            jenkinsOverviewRemoteRepository.loadNodes()
                    .toObservable()
                    .map { jenkinsNodes -> JenkinsOverviewResult.LoadJenkinsOverviewResult.Success(jenkinsNodes.computers) }
                    .cast(JenkinsOverviewResult.LoadJenkinsOverviewResult::class.java)
                    .onErrorReturn { JenkinsOverviewResult.LoadJenkinsOverviewResult.Failure(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWith(JenkinsOverviewResult.LoadJenkinsOverviewResult.Loading)
        }
    }
}