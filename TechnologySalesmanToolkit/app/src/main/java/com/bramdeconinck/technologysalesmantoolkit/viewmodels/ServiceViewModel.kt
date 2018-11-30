package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseServiceCallback
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import javax.inject.Inject

class ServiceViewModel : InjectedViewModel(), IFirebaseServiceCallback {

    @Inject
    lateinit var firestoreAPI: FirestoreAPI

    private var services = MutableLiveData<List<Service>>()

    private var isLoading = MutableLiveData<Boolean>()

    init {
        services.value = mutableListOf()

        isLoading.value = false

        firestoreAPI.getServicesFromFirestore(this)
    }

    fun getServices(): MutableLiveData<List<Service>> { return services }

    fun getIsLoading(): MutableLiveData<Boolean> { return isLoading }

    override fun onCallBack(list: MutableList<Service>) { services.value = list }

    override fun showProgress() { isLoading.value = true }

    override fun hideProgress() { isLoading.value = false }

    override fun showMessage() { Log.d("BOOOEEE", "TIS KAPOT") }
}