package com.restodine.common.di

import com.restodine.App
import com.squareup.picasso.Picasso
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton


@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

    fun getPicasso(): Picasso

    fun getApp(): App

    @Named("API_URLS")
    fun getRetrofit(): Retrofit


    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}