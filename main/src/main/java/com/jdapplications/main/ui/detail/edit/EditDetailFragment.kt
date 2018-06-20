package com.jdapplications.main.ui.detail.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jdapplications.main.R

class EditDetailFragment : Fragment() {

    companion object {
        fun newInstance() = EditDetailFragment()
    }

    private lateinit var viewModel: EditDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.edit_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
