package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.activities.MainActivity
import com.bramdeconinck.technologysalesmantoolkit.adapters.ServiceAdapter
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseCallback
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import com.bramdeconinck.technologysalesmantoolkit.utils.Utils
import kotlinx.android.synthetic.main.fragment_service_list.*
import kotlinx.android.synthetic.main.fragment_service_list.view.*

class ServiceListFragment : Fragment(), IFirebaseCallback {

    private lateinit var firestoreApi: FirestoreAPI
    private lateinit var serviceData: MutableList<Service>
    private lateinit var serviceAdapter: ServiceAdapter

    //Variable to check whether the app is running on a tablet or not
    private var twoPane: Boolean = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        firestoreApi = FirestoreAPI()
        serviceData = mutableListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_service_list, container, false)

        //If the service detail container is not null,
        //then the app is opened with a tablet
        if (rootView.service_detail_container != null) twoPane = true

        serviceAdapter = ServiceAdapter(this, serviceData, twoPane)

        rootView.service_list.adapter = serviceAdapter

        return rootView
    }

    override fun onStart() {
        super.onStart()

        fillRecyclerview()
    }

    private fun fillRecyclerview() {
        firestoreApi.getServicesFromFirestore(this)
    }

    // De methodes van de interface IFirebaseCallback worden gebruikt
    // voor het ophalen van gegevens uit de Firebase en de
    // RecyclerView er mee te vullen.
    override fun onCallBack(list: MutableList<Service>) {
        serviceData.addAll(list)
        serviceAdapter.notifyDataSetChanged()
    }

    // Deze methode laat een loading indicator zien
    override fun showProgress() {
        if (serviceData.isEmpty()) progress_bar.visibility = View.VISIBLE
        else serviceData.clear()
    }

    // Deze methode haalt de loading indicator weg
    override fun hideProgress() {
        if (progress_bar?.visibility == View.VISIBLE) progress_bar?.visibility = View.GONE
    }

    // Deze methode toont een foutmelding indien er geen gegevens opgehaald kunnen worden.
    override fun showMessage() {
        Utils.makeToast(this.requireContext(), getString(R.string.fetching_data_error))
    }

}