package com.bramdeconinck.technologysalesmantoolkit.base

import android.arch.lifecycle.ViewModel
import com.bramdeconinck.technologysalesmantoolkit.injection.component.ViewModelComponent
import com.bramdeconinck.technologysalesmantoolkit.injection.component.DaggerViewModelComponent
import com.bramdeconinck.technologysalesmantoolkit.injection.module.NetworkModule
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.ServiceViewModel

/**
 * Base class for all ViewModels that require injection through Dagger.
 */
abstract class InjectedViewModel : ViewModel() {

    /**
     * An ViewModelComponent is required to do the actual injecting.
     * Every Component has a default builder to which you can add all
     * modules that will be needed for the injection.
     */
    private val injector: ViewModelComponent = DaggerViewModelComponent
            .builder()
            .networkModule(NetworkModule)
            .build()

    /**
     * Perform the injection when the ViewModel is created
     */
    init {
        inject()
    }

    /**
     * Injects the required dependencies.
     * We need the 'when(this)' construct for each new ViewModel as the 'this' reference should
     * refer to an instance of that specific ViewModel.
     * Just injecting into a generic InjectedViewModel is not specific enough for Dagger.
     */
    private fun inject() {
        when (this) {
            is ServiceViewModel -> injector.inject(this)
        }
    }

}