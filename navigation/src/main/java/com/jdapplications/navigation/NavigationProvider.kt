package com.jdapplications.navigation

interface NavigationProvider {
    fun provideProfileNavigator(): ProfileNavigator
}