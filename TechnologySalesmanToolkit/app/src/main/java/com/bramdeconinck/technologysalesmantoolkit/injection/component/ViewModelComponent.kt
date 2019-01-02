package com.bramdeconinck.technologysalesmantoolkit.injection.component

import com.bramdeconinck.technologysalesmantoolkit.injection.module.NetworkModule
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.*
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing the inject functions for presenters.
 * Components act as the bridge between the Modules that know how to provide dependencies
 * and the actual objects that require something to be injected.
 *
 * More info can be found in [the documentation](https://google.github.io/dagger/api/2.14/dagger/Component.html).
 */
@Singleton
/**
 * All modules that are required to perform the injections into the listed objects should be listed in this annotation.
 */
@Component(modules = [NetworkModule::class])
interface ViewModelComponent {

    /**
     * Injects the dependencies into the specified ViewModel.
     */
    fun inject(serviceViewModel: ServiceViewModel)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(registrationViewModel: RegistrationViewModel)
    fun inject(settingsViewModel: SettingsViewModel)
    fun inject(profileViewModel: ProfileViewModel)
}