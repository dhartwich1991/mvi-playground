package com.jdapplications.jenkins

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jdapplications.jenkins.data.Computer
import com.jdapplications.mvi_core.mvi.MviView
import com.pedrogomez.renderers.RVRendererAdapter
import com.pedrogomez.renderers.RendererBuilder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.jenkins_overview_fragment.*
import kotlinx.android.synthetic.main.jenkins_overview_fragment.view.*


class JenkinsOverviewFragment : Fragment(), MviView<JenkinsOverviewIntent, JenkinsOverviewViewState> {
    private val disposables: CompositeDisposable = CompositeDisposable()

    private var rendererAdapter: RVRendererAdapter<Any>? = null

    private lateinit var viewModel: JenkinsOverviewViewModel

    override fun intents(): Observable<JenkinsOverviewIntent> {
        return Observable.merge(initialIntent(), refreshIntent())
    }

    override fun render(state: JenkinsOverviewViewState) {
        Log.d("JenkinsState", state.toString())

        swipeToRefreshContainer.isRefreshing = state.loading
        rendererAdapter?.diffUpdate(state.computers)
    }

    companion object {
        fun newInstance() = JenkinsOverviewFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rendererBuilder = RendererBuilder<Any>().bind(Computer::class.java, ComputerRenderer())

        rendererAdapter = RVRendererAdapter<Any>(rendererBuilder)
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        return inflater.inflate(R.layout.jenkins_overview_fragment, container, false).also {
            it.computerList.apply {
                val recyclerDividerItemDecoration = DividerItemDecoration(it.computerList.context,
                        linearLayoutManager.orientation)
                addItemDecoration(recyclerDividerItemDecoration)
                layoutManager = linearLayoutManager
                adapter = rendererAdapter
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders
                .of(this, JenkinsOverviewViewModelFactory.getInstance(requireContext()))
                .get(JenkinsOverviewViewModel::class.java)

        viewModel.processIntents(intents())

        disposables.add(viewModel.states().subscribe { viewState -> this.render(viewState) })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initialIntent(): Observable<JenkinsOverviewIntent.InitialIntent> {
        return Observable.just(JenkinsOverviewIntent.InitialIntent)
    }

    private fun refreshIntent(): Observable<JenkinsOverviewIntent.RefreshIntent> {
        val swipeRefreshLayout = swipeToRefreshContainer as SwipeRefreshLayout
        return RxSwipeRefreshLayout.refreshes(swipeRefreshLayout).map { JenkinsOverviewIntent.RefreshIntent }
    }

}
