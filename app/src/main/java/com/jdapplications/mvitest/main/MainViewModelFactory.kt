package com.jdapplications.mvitest.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jdapplications.core.SingletonHolderSingleArg
import com.jdapplications.mvitest.main.ui.main.MainActionProcessorHolder
import com.jdapplications.mvitest.main.ui.main.MainViewModel

class MainViewModelFactory private constructor(private val applicationContext: Context)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == MainViewModel::class.java) {
            return MainViewModel(MainActionProcessorHolder()) as T
        }
        throw IllegalArgumentException("unknown model class " + modelClass)
    }

    companion object : SingletonHolderSingleArg<MainViewModelFactory, Context>(::MainViewModelFactory)
}