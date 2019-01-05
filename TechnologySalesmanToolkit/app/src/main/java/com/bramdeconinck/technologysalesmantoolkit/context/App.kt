package com.bramdeconinck.technologysalesmantoolkit.context

import android.support.multidex.MultiDexApplication
import com.bramdeconinck.technologysalesmantoolkit.injection.DaggerViewModelComponent
import com.bramdeconinck.technologysalesmantoolkit.injection.ViewModelComponent
import com.bramdeconinck.technologysalesmantoolkit.injection.NetworkModule

/**
 * The [App] class is used to construct a [NetworkModule] with the application context,
 * which is used to create a local database with Room.
 */
class App: MultiDexApplication() {

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

    /**
     * The [injector] is placed in a companion object because ViewModels will
     * need it to inject themselves.
     */
    companion object { lateinit var injector: ViewModelComponent }
}