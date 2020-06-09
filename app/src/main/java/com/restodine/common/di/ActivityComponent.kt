package com.restodine.common.di

import android.app.Activity
import dagger.BindsInstance
import dagger.Component

@FeatureScope
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class)])
interface ActivityComponent {

    @Component.Builder
    interface Builder {
        fun appComponent(appComponent: AppComponent): Builder
        fun build(): ActivityComponent
        @BindsInstance
        fun activity(activity: Activity): Builder
    }

}