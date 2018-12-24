package com.bramdeconinck.technologysalesmantoolkit.context

import android.app.Application
import com.bramdeconinck.technologysalesmantoolkit.injection.component.DaggerViewModelComponent
import com.bramdeconinck.technologysalesmantoolkit.injection.component.ViewModelComponent
import com.bramdeconinck.technologysalesmantoolkit.injection.module.NetworkModule

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        injector = DaggerViewModelComponent
            .builder()
            .networkModule(NetworkModule(this))
            .build()
    }

    companion object { lateinit var injector: ViewModelComponent }
}