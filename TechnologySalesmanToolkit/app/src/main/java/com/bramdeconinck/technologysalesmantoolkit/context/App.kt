package com.bramdeconinck.technologysalesmantoolkit.context

import android.app.Application
import com.bramdeconinck.technologysalesmantoolkit.injection.component.DaggerViewModelComponent
import com.bramdeconinck.technologysalesmantoolkit.injection.component.ViewModelComponent
import com.bramdeconinck.technologysalesmantoolkit.injection.module.NetworkModule

/**
 * The [App] class is used to construct a [NetworkModule] with the application context,
 * which is used to create a local database with Room.
 */
class App: Application() {

    /**
     * A ViewModelComponent is required to do the actual injecting.
     * Every Component has a default builder to which you can add all
     * modules that will be needed for the injection.
     */
    override fun onCreate() {
        super.onCreate()
        injector = DaggerViewModelComponent
            .builder()
            .networkModule(NetworkModule(this))
            .build()
    }

    companion object { lateinit var injector: ViewModelComponent }
}