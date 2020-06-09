package com.restodine.login.di



import com.restodine.MovieDetailActivity
import com.restodine.MoviesListActivity
import com.restodine.MoviesListFragment
import com.restodine.common.di.AppComponent
import com.restodine.common.di.FeatureScope
import dagger.Component


@FeatureScope
@Component(
        modules = [LoginModule::class], dependencies = [(AppComponent::class)])
interface LoginComponent {

    @Component.Builder
    interface Builder {
        fun appComponent(appComponent: AppComponent): Builder

        fun build(): LoginComponent
    }

    fun inject(activity: MoviesListActivity)
    fun inject(fragment: MoviesListFragment)
    fun inject(movieDetail: MovieDetailActivity)
}