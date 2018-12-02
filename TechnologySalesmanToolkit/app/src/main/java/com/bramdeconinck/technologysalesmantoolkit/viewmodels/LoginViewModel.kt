package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import javax.inject.Inject

class LoginViewModel : InjectedViewModel() {

    @Inject
    lateinit var firestoreAPI: FirestoreAPI

    @Inject
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context

    init {

    }
}