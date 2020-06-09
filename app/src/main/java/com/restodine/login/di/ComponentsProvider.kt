package com.restodine.login.di

import com.restodine.App


class ComponentsProvider {
    companion object {
        fun getLoginComponent() = DaggerLoginComponent.builder().appComponent(App.appComponent).build()
    }
}