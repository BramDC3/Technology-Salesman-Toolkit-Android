package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseServiceCallback
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import javax.inject.Inject

class ServiceViewModel : InjectedViewModel(), IFirebaseServiceCallback {

    private var serviceData = MutableLiveData<MutableList<Service>>()

    @Inject
    lateinit var firestoreAPI: FirestoreAPI

    init {
        Log.d("HALLO", "IK BEN HIER")
        serviceData.value = mutableListOf()

        firestoreAPI.getServicesFromFirestore(this)
    }

    fun getServices(): MutableLiveData<MutableList<Service>> {
        return serviceData
    }

    override fun showProgress() {
        Log.d("HALLO", "SHOW PROGRESS")
    }

    override fun hideProgress() {
        Log.d("HALLO", "HIDE PROGRESS")
    }

    override fun showMessage() {
        Log.d("BOOOEEE", "TIS KAPOT")
    }

    override fun onCallBack(list: MutableList<Service>) {
        serviceData.value!!.addAll(list)
    }

}