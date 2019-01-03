package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.interfaces.FirebaseInstructionCallback
import com.bramdeconinck.technologysalesmantoolkit.interfaces.FirebaseServiceCallback
import com.bramdeconinck.technologysalesmantoolkit.models.*
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import com.bramdeconinck.technologysalesmantoolkit.repositories.InstructionRepository
import com.bramdeconinck.technologysalesmantoolkit.repositories.ServiceRepository
import com.bramdeconinck.technologysalesmantoolkit.utils.SingleLiveEvent
import org.jetbrains.anko.doAsync
import javax.inject.Inject

/**
 * Instance of [InjectedViewModel] that contains data and functions used in the
 * ServiceListFragment, ServiceDetailFragment and ServiceInstructionFragment.
 */
class ServiceViewModel : InjectedViewModel(), FirebaseServiceCallback, FirebaseInstructionCallback {

    /**
     * Injection of the [firestoreAPI], [serviceRepository] and [instructionRepository].
     */
    @Inject
    lateinit var firestoreAPI: FirestoreAPI

    @Inject
    lateinit var serviceRepository: ServiceRepository

    @Inject
    lateinit var instructionRepository: InstructionRepository

    /**
     * List of [Service] that won't be affected by filters.
     */
    private val allServices = MutableLiveData<List<Service>>()

    /**
     * List of [Service] affected by filters.
     */
    private val _services = MutableLiveData<List<Service>>()
    val services: LiveData<List<Service>>
        get() = _services

    /**
     * List of [Service] retrieved from the local database.
     */
    private val _roomServices: LiveData<List<Service>>
    val roomServices: LiveData<List<Service>>
        get() = _roomServices

    /**
     * List of [Instruction] affected by filters.
     */
    private val _instructions = MutableLiveData<List<Instruction>>()
    val instructions: LiveData<List<Instruction>>
        get() = _instructions

    /**
     * List of [Instruction] retrieved from the local database.
     */
    private val _roomInstructions: LiveData<List<Instruction>>
    val roomInstructions: LiveData<List<Instruction>>
        get() = _roomInstructions

    /**
     * Value that indicates whether services are being fetched by the [firestoreAPI] or not.
     */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    /**
     * The selected service for the current ServiceDetailFragment.
     */
    val selectedService = MutableLiveData<Service>()

    /**
     * Filters for services.
     */
    private var selectedCategory: Category?
    private var searchQuery: String

    /**
     * [SingleLiveEvent] objects used to emit events.
     */
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

    /**
     * Function to modify the list of [Service] that should be displayed.
     */
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
        _services.value = allServices.value
    }

    fun fetchServices() {
        clearFilters()
        firestoreAPI.fetchAllServices(this)
    }

    fun fetchInstructions(serviceId: String) {
        firestoreAPI.fetchAllInstructionsFrom(serviceId, this)
    }

    fun clearInstructions() { _instructions.value = mutableListOf() }

    fun getInstructionById(instructionId: String) : Instruction { return instructions.value!!.first { it.id == instructionId } }

    /**
     * Function that gets called after the services from the database are loaded.
     */
    fun onDatabaseServicesReady() {
        if (allServices.value!!.isEmpty() && roomServices.value!!.isNotEmpty()) {
            allServices.value = roomServices.value
            _isLoading.value = false
            refreshServiceList()
        }
    }

    /**
     * Function that gets called after the instructions from the database are loaded.
     */
    fun onDatabaseInstructionsReady(id: String) {
        if (_instructions.value!!.isEmpty() && !roomInstructions.value.isNullOrEmpty()) {
            _instructions.value = roomInstructions.value!!.filter { it.serviceId == id }
        }
    }

    /**
     * Function that updates the list of services after fetching them and also inserts them into the local database.
     */
    override fun onServicesCallBack(list: List<Any>) {
        allServices.value = list.map { it as Service }
        doAsync { serviceRepository.insert(allServices.value!!) }
        refreshServiceList()
    }

    override fun showProgress() { _isLoading.value = true }

    override fun hideProgress() { _isLoading.value = false }

    override fun showServicesMessage() { servicesErrorOccurred.call() }

    /**
     * Function that updates the list of instructions after fetching them and also inserts them into the local database.
     */
    override fun onInstructionsCallBack(list: List<Any>) {
        _instructions.value = list.map { it as Instruction }
        doAsync {
            instructionRepository.deleteInstructionsByServiceId(_instructions.value!![0].serviceId)
            instructionRepository.insert(_instructions.value!!)
        }
    }

    override fun showInstructionsMessage() { instructionsErrorOccurred.call() }

}