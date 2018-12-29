package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.interfaces.FirebaseInstructionCallback
import com.bramdeconinck.technologysalesmantoolkit.interfaces.FirebaseServiceCallback
import com.bramdeconinck.technologysalesmantoolkit.models.*
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import com.bramdeconinck.technologysalesmantoolkit.utils.SingleLiveEvent
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class ServiceViewModel : InjectedViewModel(), FirebaseServiceCallback, FirebaseInstructionCallback {

    @Inject
    lateinit var firestoreAPI: FirestoreAPI

    @Inject
    lateinit var serviceRepository: ServiceRepository

    @Inject
    lateinit var instructionRepository: InstructionRepository

    private val allServices = MutableLiveData<List<Service>>()

    private val _services = MutableLiveData<List<Service>>()
    val services: MutableLiveData<List<Service>>
        get() = _services

    private val _roomServices: LiveData<List<Service>>
    val roomServices: LiveData<List<Service>>
        get() = _roomServices

    private val _instructions = MutableLiveData<List<Instruction>>()
    val instructions: MutableLiveData<List<Instruction>>
        get() = _instructions

    val selectedService = MutableLiveData<Service>()

    private val _roomInstructions: LiveData<List<Instruction>>
    val roomInstructions: LiveData<List<Instruction>>
        get() = _roomInstructions

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var selectedCategory: Category?

    private var searchQuery: String

    val servicesErrorOccurred = SingleLiveEvent<Any>()

    val instructionsErrorOccurred = SingleLiveEvent<Any>()

    init {
        allServices.value = mutableListOf()

        _services.value = mutableListOf()

        _roomServices = serviceRepository.services

        _instructions.value = mutableListOf()

        _roomInstructions = instructionRepository.instructions

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
        services.value = allServices.value
    }

    fun fetchServices() {
        clearFilters()
        firestoreAPI.fetchAllServices(this)
    }

    fun fetchInstructions(serviceId: String) {
        firestoreAPI.fetchAllInstructionsFrom(serviceId, this)
    }

    fun clearInstructions() { _instructions.value = mutableListOf() }

    fun onDatabaseServicesReady() {
        if (allServices.value!!.isEmpty() && roomServices.value!!.isNotEmpty()) {
            allServices.value = roomServices.value
            _isLoading.value = false
            refreshServiceList()
        }
    }

    fun onDatabaseInstructionsReady(id: String) {
        if (_instructions.value!!.isEmpty() && !roomInstructions.value.isNullOrEmpty()) {
            _instructions.value = roomInstructions.value!!.filter { it.serviceId == id }
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

    override fun onInstructionsCallBack(list: List<Any>) {
        _instructions.value = list.map { it as Instruction }
        doAsync {
            instructionRepository.deleteInstructionsByServiceId(_instructions.value!![0].serviceId)
            instructionRepository.insert(_instructions.value!!)
        }
    }

    override fun showInstructionsMessage() { instructionsErrorOccurred.call() }

}