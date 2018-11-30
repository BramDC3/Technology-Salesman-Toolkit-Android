package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.adapters.ServiceAdapter
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.ServiceViewModel
import kotlinx.android.synthetic.main.fragment_service_list.view.*

class ServiceListFragment : Fragment() {

    private lateinit var serviceAdapter: ServiceAdapter
    private lateinit var serviceViewModel: ServiceViewModel

    // Variable to check whether the app is running on a tablet or not
    private var twoPane: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_service_list, container, false)

        serviceViewModel = ViewModelProviders.of(activity!!).get(ServiceViewModel::class.java)

        // If the service detail container is not null,
        // then the app is running on a tablet
        if (rootView.service_detail_container != null) twoPane = true

        val services = serviceViewModel.getServices()

        val isLoading = serviceViewModel.getIsLoading()

        serviceAdapter = ServiceAdapter(this, services, twoPane)

        services.observe(this, Observer {
            serviceAdapter.notifyDataSetChanged()
        })

        isLoading.observe(this, Observer {
            if (isLoading.value!!) rootView.progress_bar.visibility = View.VISIBLE
            else rootView.progress_bar.visibility = View.GONE
        })

        rootView.service_list.adapter = serviceAdapter

        return rootView
    }

}