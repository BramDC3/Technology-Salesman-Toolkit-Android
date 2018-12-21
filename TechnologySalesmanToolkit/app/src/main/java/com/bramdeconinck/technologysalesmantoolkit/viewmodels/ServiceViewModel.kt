package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseInstructionCallback
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseServiceCallback
import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.models.ServiceRepository
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import com.bramdeconinck.technologysalesmantoolkit.utils.SingleLiveEvent
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class ServiceViewModel : InjectedViewModel(), IFirebaseServiceCallback, IFirebaseInstructionCallback {

    @Inject
    lateinit var firestoreAPI: FirestoreAPI

    @Inject
    lateinit var serviceRepository: ServiceRepository

    private val allServices = MutableLiveData<List<Service>>()

    private val _roomServices: LiveData<List<Service>>
    val roomServices: LiveData<List<Service>>
        get() = _roomServices

    private val _services = MutableLiveData<List<Service>>()
    val services: MutableLiveData<List<Service>>
        get() = _services

    private val _instructions = MutableLiveData<List<Instruction>>()
    val instructions: MutableLiveData<List<Instruction>>
        get() = _instructions

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var selectedCategory: Category?

    private var searchQuery: String

    val servicesErrorOccurred = SingleLiveEvent<Any>()

    val instructionsErrorOccurred = SingleLiveEvent<Any>()

    init {
        allServices.value = mutableListOf()

        _roomServices = serviceRepository.services

        _services.value = mutableListOf()

        _instructions.value = mutableListOf()

        _isLoading.value = false

        selectedCategory = null

        searchQuery = ""

        fetchServices()
    }

    private fun refreshServiceList() {
        if (selectedCategory != null) _services.value = allServices.value!!.filter { it.name.toLowerCase().contains(searchQuery) && it.category == selectedCategory }
        else _services.value = allServices.value!!.filter { it.name.toLowerCase().contains(searchQuery) }
    }

    fun applySearchQuery(query: String) {
        searchQuery = query.toLowerCase()
        refreshServiceList()
    }

    fun applyCategoryQuery(category: Category?) {
        selectedCategory = category
        refreshServiceList()
    }

    fun clearFilters() {
        selectedCategory = null
        searchQuery = ""
    }

    fun fetchServices() {
        clearFilters()
        firestoreAPI.getAllServices(this)
    }

    fun fetchInstructions(serviceId: String) { firestoreAPI.getAllInstructionsFrom(serviceId, this) }

    fun onDatabaseReady() {
        if (allServices.value!!.isEmpty() && roomServices.value!!.isNotEmpty()) {
            allServices.value = roomServices.value
            refreshServiceList()
        }
    }

    override fun onServicesCallBack(list: List<Any>) {
        allServices.value = list.map { it as Service }
        doAsync { serviceRepository.insert(allServices.value!!) }
        refreshServiceList()
    }

    override fun showProgress() { _isLoading.value = true }

    override fun hideProgress() { _isLoading.value = false }

    override fun showServicesMessage() { servicesErrorOccurred.call() }

    override fun onInstructionsCallBack(list: List<Any>) { _instructions.value = list.map { it as Instruction } }

    override fun showInstructionsMessage() { instructionsErrorOccurred.call() }

}