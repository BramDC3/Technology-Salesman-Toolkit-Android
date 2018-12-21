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

    private var services = MutableLiveData<List<Service>>()

    private var instructions = MutableLiveData<List<Instruction>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val servicesErrorOccurred = SingleLiveEvent<Any>()

    val instructionsErrorOccurred = SingleLiveEvent<Any>()

    init {
        services.value = mutableListOf()

        instructions.value = mutableListOf()

        _isLoading.value = false

        firestoreAPI.getAllServices(this)
    }

    fun getServices(): MutableLiveData<List<Service>> { return services }

    fun getInstructions(): MutableLiveData<List<Instruction>> { return instructions }

    fun fetchInstructions(serviceId: String) { firestoreAPI.getAllInstructionsFrom(serviceId, this) }

    override fun onServicesCallBack(list: List<Any>) { services.value = list.map { it as Service } }

    override fun showProgress() { _isLoading.value = true }

    override fun hideProgress() { _isLoading.value = false }

    override fun showServicesMessage() { servicesErrorOccurred.call() }

    override fun onInstructionsCallBack(list: List<Any>) { instructions.value = list.map { it as Instruction } }

    override fun showInstructionsMessage() { instructionsErrorOccurred.call() }

}