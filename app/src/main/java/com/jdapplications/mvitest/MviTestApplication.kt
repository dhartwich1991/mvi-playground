package com.jdapplications.mvitest

import android.app.Application
import android.view.View
import androidx.navigation.Navigation
import com.jdapplications.navigation.NavigationProvider
import com.jdapplications.navigation.ProfileNavigator

class MviTestApplication : Application(), NavigationProvider {

    override fun provideProfileNavigator(): ProfileNavigator {
        return object : ProfileNavigator {
            override fun showMain(view: View) {
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_mainFragment)
            }
        }
    }

}