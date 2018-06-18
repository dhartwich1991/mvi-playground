package com.jdapplications.mvitest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jdapplications.mvitest.ui.main.MainActionProcessorHolder
import com.jdapplications.mvitest.ui.main.MainViewModel

class MviTestViewModelFactory private constructor(private val applicationContext: Context)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == MainViewModel::class.java) {
            return MainViewModel(MainActionProcessorHolder()) as T
        }
        throw IllegalArgumentException("unknown model class " + modelClass)
    }

    companion object : SingletonHolderSingleArg<MviTestViewModelFactory, Context>(::MviTestViewModelFactory)
}