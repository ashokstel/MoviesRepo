package com.restodine.login.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.restodine.common.di.ViewModelFactory
import com.restodine.common.di.ViewModelKey
import com.restodine.login.viewmodel.MovieViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap



@Module
interface LoginViewModule {

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    fun bindLoginViewModel(loginViewModel: MovieViewModel): ViewModel
    @Binds
     abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}