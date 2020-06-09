package com.restodine.common.di

import android.content.SharedPreferences
import com.restodine.App
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton


@Module(includes = [DatabaseModule::class, NetworkModule::class, ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideApplication(): App = App.app

    @Provides
    @Singleton
    fun providePicasso(app: App): Picasso = Picasso.Builder(app).build()

}
