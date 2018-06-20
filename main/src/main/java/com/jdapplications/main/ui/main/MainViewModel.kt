package com.jdapplications.main.ui.main

import androidx.lifecycle.ViewModel
import com.jdapplications.core.notOfType
import com.jdapplications.mvi_core.mvi.MviViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class MainViewModel(private val mainActionProcessorHolder: MainActionProcessorHolder) : ViewModel(), MviViewModel<MainIntent, MainViewState> {

    /**
     * Proxy subject used to keep the stream alive even after the UI gets recycled.
     * This is basically used to keep ongoing events and the last cached State alive
     * while the UI disconnects and reconnects on config changes.
     */
    private val intentsSubject: PublishSubject<MainIntent> = PublishSubject.create()
    private val statesObservable: Observable<MainViewState> = compose()


    override fun processIntents(intents: Observable<MainIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<MainViewState> = statesObservable

    /**
     * Compose all components to create the stream logic
     */
    private fun compose(): Observable<MainViewState> {
        return intentsSubject
                .compose<MainIntent>(intentFilter)
                .map { intent -> actionFromIntent(intent) }
                .compose(mainActionProcessorHolder.actionProcessor)
                // Cache each state and pass it to the reducer to create a new state from
                // the previous cached one and the latest Result emitted from the action processor.
                // The Scan operator is used here for the caching.
                .scan(MainViewState.initial(), reducer)
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
    private fun actionFromIntent(intent: MainIntent): MainAction {
        return when (intent) {
            is MainIntent.InitialIntent -> MainAction.ShowHelloWorldAction
            is MainIntent.ShowUsernameIntent -> MainAction.ShowUsernameAction
            is MainIntent.SubmitFeedbackIntent -> MainAction.SubmitFeedbackAction(intent.feedback)
            is MainIntent.LoadUsersIntent -> MainAction.LoadUsersAction
        }
    }


    /**
     * take only the first ever InitialIntent and all intents of other types
     * to avoid reloading data on config changes
     */
    private val intentFilter: ObservableTransformer<MainIntent, MainIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<MainIntent>(
                        shared.ofType(MainIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(MainIntent.InitialIntent::class.java)
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
        private val reducer = BiFunction { previousState: MainViewState, result: MainResult ->
            when (result) {
                is MainResult ->
                    when (result) {
                        is MainResult.LoadWelcomeTextResults.Success ->
                            previousState.copy(username = result.username)
                        is MainResult.LoadWelcomeTextResults.Failure ->
                            previousState.copy()
                        is MainResult.SubmitFeedbackResults.Success -> previousState.copy(feedbackSubmitted = true,
                                loading = false, error = false)
                        is MainResult.SubmitFeedbackResults.Submitting -> previousState.copy(loading = true, error = false)
                        is MainResult.SubmitFeedbackResults.Failure -> previousState.copy(feedbackSubmitted = false,
                                loading = false, error = true)
                        is MainResult.LoadUserListResults.Success -> previousState.copy(userList = result.list, loading =
                        false)
                        is MainResult.LoadUserListResults.Failure -> previousState.copy(error = true, loading =
                        false)
                        is MainResult.LoadUserListResults.Loading -> previousState.copy(loading = true)
                    }
            }
        }
    }


}
