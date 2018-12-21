package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseInstructionCallback
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseServiceCallback
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import com.bramdeconinck.technologysalesmantoolkit.utils.SingleLiveEvent
import javax.inject.Inject

class ServiceViewModel : InjectedViewModel(), IFirebaseServiceCallback, IFirebaseInstructionCallback {

    @Inject
    lateinit var firestoreAPI: FirestoreAPI

    private val allServices = MutableLiveData<List<Service>>()

    private val _services = MutableLiveData<List<Service>>()
    val services: MutableLiveData<List<Service>>
        get() = _services

    private val _instructions = MutableLiveData<List<Instruction>>()
    val instructions: MutableLiveData<List<Instruction>>
        get() = _instructions

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val servicesErrorOccurred = SingleLiveEvent<Any>()

    val instructionsErrorOccurred = SingleLiveEvent<Any>()

    init {
        allServices.value = mutableListOf()

        _services.value = mutableListOf()

        _instructions.value = mutableListOf()

        _isLoading.value = false

        firestoreAPI.getAllServices(this)
    }

    fun fetchInstructions(serviceId: String) { firestoreAPI.getAllInstructionsFrom(serviceId, this) }

    fun applySearchQuery(query: String) {
        if (query.isEmpty()) _services.value = allServices.value
        else _services.value = allServices.value!!.filter { it.name.toLowerCase().contains(query.toLowerCase()) }
    }

    override fun onServicesCallBack(list: List<Any>) {
        allServices.value = list.map { it as Service }
        _services.value = allServices.value
    }

    override fun showProgress() { _isLoading.value = true }

    override fun hideProgress() { _isLoading.value = false }

    override fun showServicesMessage() { servicesErrorOccurred.call() }

    override fun onInstructionsCallBack(list: List<Any>) { _instructions.value = list.map { it as Instruction } }

    override fun showInstructionsMessage() { instructionsErrorOccurred.call() }

}