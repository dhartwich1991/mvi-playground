package com.jdapplications.main.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.jakewharton.rxbinding2.view.RxView
import com.jdapplications.main.MainViewModelFactory
import com.jdapplications.main.R
import com.jdapplications.mvi_core.mvi.MviView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment(), MviView<MainIntent, MainViewState> {
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun intents(): Observable<MainIntent> {
        return Observable.merge(initialIntent(), buttonClickedIntent(), submitFeedbackIntent(), loadUsersIntent())
    }

    private fun initialIntent(): Observable<MainIntent.InitialIntent> {
        return Observable.just(MainIntent.InitialIntent)
    }

    private fun buttonClickedIntent(): Observable<MainIntent.ShowUsernameIntent> {
        return RxView.clicks(button).map {
            MainIntent.ShowUsernameIntent
        }
    }

    private fun submitFeedbackIntent(): Observable<MainIntent.SubmitFeedbackIntent> {
        return RxView.clicks(submitFeedback).map {
            MainIntent.SubmitFeedbackIntent(feedback.text.toString())
        }
    }

    private fun loadUsersIntent(): Observable<MainIntent.LoadUsersIntent> {
        return RxView.clicks(loadUsers).map {
            MainIntent.LoadUsersIntent
        }
    }

    override fun render(state: MainViewState) {
        if (state.loading) {
            message.text = "Loading"
            return
        }

        if (state.userList.isNotEmpty()) {
            message.text = "User List is loaded!"
            return
        }

        if (state.error) {
            message.text = "Error"
            return
        }

        if (state.feedbackSubmitted) {
            message.text = "Feedback submitted"
            button.isEnabled = false
        }
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false).also { view ->
            view.navigateButton.setOnClickListener { buttonView ->
                Navigation.findNavController(buttonView).navigate(R.id.action_mainFragment_to_detailFragment)
            }

            view.profileButton.setOnClickListener { buttonView ->
                Navigation.findNavController(buttonView).navigate(R.id.action_mainFragment_to_profileFragment)
            }

            view.jenkinsButton.setOnClickListener { buttonView ->
                Navigation.findNavController(buttonView).navigate(R.id.action_mainFragment_to_jenkinsOverviewFragment)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders
                .of(this, MainViewModelFactory.getInstance(requireContext()))
                .get(MainViewModel::class.java)

        disposables.add(viewModel.states().subscribe({ viewState -> this.render(viewState) }, {}))

        viewModel.processIntents(intents())
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}
