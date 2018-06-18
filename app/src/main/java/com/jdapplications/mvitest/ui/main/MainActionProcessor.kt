package com.jdapplications.mvitest.ui.main

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import java.util.*
import java.util.concurrent.TimeUnit

class MainActionProcessorHolder {

    /**
     * Splits the [Observable] to match each type of [MviAction] to
     * its corresponding business logic processor. Each processor takes a defined [MviAction],
     * returns a defined [MviResult]
     * The global actionProcessor then merges all [Observable] back to
     * one unique [Observable].
     *
     *
     * The splitting is done using [Observable.publish] which allows almost anything
     * on the passed [Observable] as long as one and only one [Observable] is returned.
     *
     *
     * An security layer is also added for unhandled [MviAction] to allow early crash
     * at runtime to easy the maintenance.
     */
    internal val actionProcessor = ObservableTransformer<MainAction, MainResult> { actions ->
        actions.publish { shared ->
            Observable.merge<MainResult>(
                    shared.ofType(MainAction.ShowHelloWorldAction::class.java).compose(helloWorldActionProcessor),
                    shared.ofType(MainAction.ShowUsernameAction::class.java).compose(showUsernameProcessor),
                    shared.ofType(MainAction.SubmitFeedbackAction::class.java).compose(submitFeedbackProcessor),
                    shared.ofType(MainAction.LoadUsersAction::class.java).compose(loadUsersProcessor))

        }
    }

    private val helloWorldActionProcessor =
            ObservableTransformer<MainAction.ShowHelloWorldAction, MainResult.LoadWelcomeTextResults> { actions ->
                actions.flatMap { action ->
                    Observable.just(MainResult.LoadWelcomeTextResults.Success(username = "UserBob"))
                }
            }

    private val showUsernameProcessor =
            ObservableTransformer<MainAction.ShowUsernameAction, MainResult.LoadWelcomeTextResults> { actions ->
                actions.flatMap { action ->
                    Observable.just(MainResult.LoadWelcomeTextResults.Success(username = getRandom()))
                }
            }

    private val submitFeedbackProcessor =
            ObservableTransformer<MainAction.SubmitFeedbackAction, MainResult.SubmitFeedbackResults> { actions ->
                actions.flatMap { action ->
                    if (action.feedback == "") {
                        Observable.just<MainResult.SubmitFeedbackResults>(MainResult.SubmitFeedbackResults.Submitting)
                                .mergeWith(Observable.just(MainResult.SubmitFeedbackResults.Failure))

                    } else {
                        Observable.just<MainResult.SubmitFeedbackResults>(MainResult.SubmitFeedbackResults.Submitting)
                                .mergeWith(Observable.just(MainResult.SubmitFeedbackResults.Success))
                    }
                }
            }

    private val loadUsersProcessor =
            ObservableTransformer<MainAction.LoadUsersAction, MainResult.LoadUserListResults> { actions ->
                actions.flatMap { action ->
                    Observable.just(listOf("Hello", "World", "How", "Are", "You"))
                            .delay(500, TimeUnit.MILLISECONDS)
                            .map { users -> MainResult.LoadUserListResults.Success(users) }
                            .cast(MainResult.LoadUserListResults::class.java)
                            .onErrorReturn { MainResult.LoadUserListResults.Failure }
                            .startWith(MainResult.LoadUserListResults.Loading)
                }
            }

//    private val loadTasksProcessor =
//            ObservableTransformer<LoadTasksAction, LoadTasksResult> { actions ->
//                actions.flatMap { action ->
//                    tasksRepository.getTasks(action.forceUpdate)
//                            // Transform the Single to an Observable to allow emission of multiple
//                            // events down the stream (e.g. the InFlight event)
//                            .toObservable()
//                            // Wrap returned data into an immutable object
//                            .map { tasks -> LoadTasksResult.Success(tasks, action.filterType) }
//                            .cast(LoadTasksResult::class.java)
//                            // Wrap any error into an immutable object and pass it down the stream
//                            // without crashing.
//                            // Because errors are data and hence, should just be part of the stream.
//                            .onErrorReturn(LoadTasksResult::Failure)
//                            .subscribeOn(schedulerProvider.io())
//                            .observeOn(schedulerProvider.ui())
//                            // Emit an InFlight event to notify the subscribers (e.g. the UI) we are
//                            // doing work and waiting on a response.
//                            // We emit it after observing on the UI thread to allow the event to be emitted
//                            // on the current frame and avoid jank.
//                            .startWith(TasksResult.LoadTasksResult.InFlight)
//                }
//            }

    fun getRandom(): String {
        return UUID.randomUUID().toString()
    }
}