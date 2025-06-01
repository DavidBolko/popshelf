package com.example.popshelf

import android.app.Application

/**
 * Custom [Application] class for the whole Popshelf application.
 *
 *  Only goal of this class is to initialize [AppContainer] which is needed for managing application dependencies.
 */
class PopshelfApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(applicationContext)
    }
}