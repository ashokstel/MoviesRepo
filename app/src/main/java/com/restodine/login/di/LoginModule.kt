package com.restodine.login.di


import com.restodine.common.db.MoviesDatabase
import com.restodine.common.di.FeatureScope
import com.restodine.login.api.MovieApi
import com.restodine.login.dao.MoviesDao
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Named


@Module(includes = [(LoginViewModule::class)])
class LoginModule {

    @Provides
    @FeatureScope
    fun provideLoginApi(@Named("API_URLS") retrofit: Retrofit): MovieApi {
        Timber.d("Preparing new instance for LoginApi(API_URLS)")
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    @FeatureScope
    fun provideBusinessMoviesDao(): MoviesDao = MoviesDatabase.instance.moviesDao

}
