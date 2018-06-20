package com.jdapplications.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.jdapplications.navigation.NavigationProvider
import kotlinx.android.synthetic.main.profile_fragment.view.*


class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
                .also { view ->
                    view.goBack.setOnClickListener {
                        val provideProfileNavigator = (requireActivity().application as NavigationProvider).provideProfileNavigator()
//                        provideProfileNavigator.showMain(it)
                        Navigation.findNavController(it).navigate(R.id.action_profileFragment_to_mainFragment)
                    }
                }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
