package com.jdapplications.mvitest.main.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.jakewharton.rxbinding2.view.RxView
import com.jdapplications.mvi_core.mvi.MviView
import com.jdapplications.mvitest.R
import com.jdapplications.mvitest.main.MainViewModelFactory
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment(), MviView<MainIntent, MainViewState> {
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun intents(): Observable<MainIntent> {
        return Observable.merge(initialIntent(), updateStatusIntent())
    }

    private fun initialIntent(): Observable<MainIntent.InitialIntent> {
        return Observable.just(MainIntent.InitialIntent)
    }

    private fun updateStatusIntent(): Observable<MainIntent.UpdateStatusIntent> {
        return RxView.clicks(statusButton).map {
            MainIntent.UpdateStatusIntent
        }
    }

    override fun render(state: MainViewState) {
        if (state.loading) {
            message.text = "Loading"
            return
        }

        if (state.userList.isNotEmpty()) {
            message.text = "Status is updated!"
            return
        }

        if (state.error) {
            message.text = "Error"
            return
        }
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false).also { view ->
            view.profileButton.setOnClickListener { buttonView ->
                Navigation.findNavController(buttonView).navigate(R.id.openProfile)
            }

            view.jenkinsButton.setOnClickListener { buttonView ->
                Navigation.findNavController(buttonView).navigate(R.id.openJenkins)
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
