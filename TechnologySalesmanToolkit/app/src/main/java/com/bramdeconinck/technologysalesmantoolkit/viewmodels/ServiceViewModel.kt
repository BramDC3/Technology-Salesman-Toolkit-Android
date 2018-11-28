package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI

class ServiceViewModel : ViewModel() {

    private val firestoreApi = FirestoreAPI()
    private var serviceData = MutableLiveData<MutableList<Service>>()

    init {
        serviceData.value = mutableListOf()
    }

}