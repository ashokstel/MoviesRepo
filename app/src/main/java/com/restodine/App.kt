package com.restodine

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.work.Configuration
import androidx.work.WorkManager
import com.facebook.stetho.Stetho
import com.restodine.common.di.AppComponent
import com.restodine.common.di.DaggerAppComponent
import timber.log.Timber



open class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var app: App
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //MultiDex.install(this)
        //appComponent = DaggerAppComponent.builder().build()
    }

    override fun onCreate() {

        super.onCreate()
        app = this
        appComponent = DaggerAppComponent.builder().build()
        initStetho()

    }

    private fun initWorkManager() {
        // provide custom configuration
        val myConfig = Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.VERBOSE)
                .build()

        // initialize WorkManager
        WorkManager.initialize(this, myConfig)
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG ) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build())
        }
    }

    /*protected open fun initTimber() {
        val debug = BuildConfig.DEBUG
        Timber.plant(if (debug) Timber.DebugTree() else CrashReportingTree())
    }*/




}