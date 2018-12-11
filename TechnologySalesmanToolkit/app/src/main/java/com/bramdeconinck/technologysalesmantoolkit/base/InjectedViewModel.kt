package com.bramdeconinck.technologysalesmantoolkit.base

import android.arch.lifecycle.ViewModel
import com.bramdeconinck.technologysalesmantoolkit.context.App.Companion.injector
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.*

/**
 * Base class for all ViewModels that require injection through Dagger.
 */
abstract class InjectedViewModel : ViewModel() {

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
            is LoginViewModel -> injector.inject(this)
            is RegistrationViewModel -> injector.inject(this)
            is SettingsViewModel -> injector.inject(this)
            is ProfileViewModel -> injector.inject(this)
        }
    }

}