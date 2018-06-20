package com.jdapplications.jenkins

import androidx.lifecycle.ViewModel;
import com.jdapplications.core.notOfType
import com.jdapplications.mvi_core.mvi.MviViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class JenkinsOverviewViewModel(private val jenkinsOverviewActionProcessorHolder: JenkinsOverviewActionProcessorHolder) : ViewModel(),
        MviViewModel<JenkinsOverviewIntent, JenkinsOverviewViewState> {

    /**
     * Proxy subject used to keep the stream alive even after the UI gets recycled.
     * This is basically used to keep ongoing events and the last cached State alive
     * while the UI disconnects and reconnects on config changes.
     */
    private val intentsSubject: PublishSubject<JenkinsOverviewIntent> = PublishSubject.create()
    private val statesObservable: Observable<JenkinsOverviewViewState> = compose()


    override fun processIntents(intents: Observable<JenkinsOverviewIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<JenkinsOverviewViewState> = statesObservable

    /**
     * Compose all components to create the stream logic
     */
    private fun compose(): Observable<JenkinsOverviewViewState> {
        return intentsSubject
                .compose<JenkinsOverviewIntent>(intentFilter)
                .map { intent -> actionFromIntent(intent) }
                .compose(jenkinsOverviewActionProcessorHolder.actionProcessor)
                // Cache each state and pass it to the reducer to create a new state from
                // the previous cached one and the latest Result emitted from the action processor.
                // The Scan operator is used here for the caching.
                .scan(JenkinsOverviewViewState.INITIAL, reducer)
                // When a reducer just emits previousState, there's no reason to call render. In fact,
                // redrawing the UI in cases like this can cause jank (e.g. messing up snackbar animations
                // by showing the same snackbar twice in rapid succession).
                .distinctUntilChanged()
                // Emit the last one event of the stream on subscription.
                // Useful when a View rebinds to the ViewModel after rotation.
                .replay(1)
                // Create the stream on creation without waiting for anyone to subscribe
                // This allows the stream to stay alive even when the UI disconnects and
                // match the stream's lifecycle to the ViewModel's one.
                .autoConnect(0)
    }

    /**
     * Translate an [MviIntent] to an [MviAction].
     * Used to decouple the UI and the business logic to allow easy testings and reusability.
     */
    private fun actionFromIntent(intent: JenkinsOverviewIntent): JenkinsOverviewAction {
        return when (intent) {
            JenkinsOverviewIntent.InitialIntent -> JenkinsOverviewAction.LoadJenkinsOverviewAction
            JenkinsOverviewIntent.RefreshIntent -> JenkinsOverviewAction.LoadJenkinsOverviewAction
        }
    }

    /**
     * take only the first ever InitialIntent and all intents of other types
     * to avoid reloading data on config changes
     */
    private val intentFilter: ObservableTransformer<JenkinsOverviewIntent, JenkinsOverviewIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<JenkinsOverviewIntent>(
                        shared.ofType(JenkinsOverviewIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(JenkinsOverviewIntent.InitialIntent::class.java)
                )
            }
        }

    companion object {
        /**
         * The Reducer is where [MviViewState], that the [MviView] will use to
         * render itself, are created.
         * It takes the last cached [MviViewState], the latest [MviResult] and
         * creates a new [MviViewState] by only updating the related fields.
         * This is basically like a big switch statement of all possible types for the [MviResult]
         */
        private val reducer = BiFunction { previousState: JenkinsOverviewViewState, result: JenkinsOverviewResult ->
            when (result) {
                is JenkinsOverviewResult.LoadJenkinsOverviewResult.Loading ->
                    previousState.copy(loading = true)
                is JenkinsOverviewResult.LoadJenkinsOverviewResult.Success ->
                    previousState.copy(loading = false, computers = result.computers)
                is JenkinsOverviewResult.LoadJenkinsOverviewResult.Failure ->
                    previousState.copy(loading = false, error = result.error)
            }
        }
    }
}
