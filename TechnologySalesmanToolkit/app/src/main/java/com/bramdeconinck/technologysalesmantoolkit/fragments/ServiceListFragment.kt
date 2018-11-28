package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.adapters.ServiceAdapter
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseServiceCallback
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.ServiceViewModel
import kotlinx.android.synthetic.main.fragment_service_list.*
import kotlinx.android.synthetic.main.fragment_service_list.view.*

class ServiceListFragment : Fragment(), IFirebaseServiceCallback {

    private lateinit var firestoreApi: FirestoreAPI
    private lateinit var serviceData: MutableList<Service>
    private lateinit var serviceAdapter: ServiceAdapter

    //private lateinit var serviceViewModel: ServiceViewModel

    // Variable to check whether the app is running on a tablet or not
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestoreApi = FirestoreAPI()
        serviceData = mutableListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_service_list, container, false)

        //serviceViewModel = ViewModelProviders.of(activity!!).get(ServiceViewModel::class.java)

        // If the service detail container is not null,
        // then the app is running on a tablet
        if (rootView.service_detail_container != null) twoPane = true

        serviceAdapter = ServiceAdapter(this, serviceData, twoPane)

        rootView.service_list.adapter = serviceAdapter

        return rootView
    }

    override fun onStart() {
        super.onStart()

        fillRecyclerview()
    }

    //Function for retrieving services from the Firestore and filling the recyclerview with them
    private fun fillRecyclerview() { firestoreApi.getServicesFromFirestore(this) }

    // The methods of the IFirebaseServiceCallback interface
    // Function to fill the the adapter with data and notify it
    override fun onCallBack(list: MutableList<Service>) {
        serviceData.addAll(list)
        serviceAdapter.notifyDataSetChanged()
    }

    // Function to show the progress indicator
    override fun showProgress() {
        if (serviceData.isEmpty()) progress_bar.visibility = View.VISIBLE
        else serviceData.clear()
    }

    // Function to hide the progress indicator
    override fun hideProgress() {
        if (progress_bar?.visibility == View.VISIBLE) progress_bar?.visibility = View.GONE
    }

    // Function to show a message when an error occurs
    override fun showMessage() {
        MessageUtils.makeToast(context!!, getString(R.string.fetching_data_error))
    }

}