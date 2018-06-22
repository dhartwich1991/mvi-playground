package com.jdapplications.mvitest.main.ui.main

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
                    shared.ofType(MainAction.InitialAction::class.java).compose(initialProcessor),
                    shared.ofType(MainAction.UpdateStatusAction::class.java).compose(updateStatusProcessor))
        }
    }

    private val updateStatusProcessor =
            ObservableTransformer<MainAction.UpdateStatusAction, MainResult.UpdateStatusResults> { actions ->
                actions.flatMap { action ->
                    Observable.just(listOf("Hello", "World", "How", "Are", "You"))
                            .delay(500, TimeUnit.MILLISECONDS)
                            .map { MainResult.UpdateStatusResults.Success }
                            .cast(MainResult.UpdateStatusResults::class.java)
                            .onErrorReturn { MainResult.UpdateStatusResults.Failure }
                            .startWith(MainResult.UpdateStatusResults.Loading)
                }
            }

    private val initialProcessor =
            ObservableTransformer<MainAction.InitialAction, MainResult.InitialResult> { actions ->
                actions.flatMap { action ->
                    Observable.just(MainResult.InitialResult)
                }
            }


    fun getRandom(): String {
        return UUID.randomUUID().toString()
    }
}