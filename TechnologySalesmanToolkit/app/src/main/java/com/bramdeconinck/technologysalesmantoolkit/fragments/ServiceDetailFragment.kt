package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IToolbarTitleListener
import com.bramdeconinck.technologysalesmantoolkit.utils.ARG_ITEM_ID
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.ServiceViewModel
import kotlinx.android.synthetic.main.fragment_service_detail.*

class ServiceDetailFragment : Fragment() {

    private var service: Service? = null
    private lateinit var serviceViewModel: ServiceViewModel
    // private lateinit var pagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = arguments?.getParcelable(ARG_ITEM_ID) as Service
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        serviceViewModel = ViewModelProviders.of(activity!!).get(ServiceViewModel::class.java)

        // pagerAdapter = FragmentPagerAdapter(childFragmentManager)

        // vp_service_detail_instructions

        return inflater.inflate(R.layout.fragment_service_detail, container, false)
    }

    override fun onStart() {
        super.onStart()

        setSupportActionBarTitle(service?.name)
    }

    private fun setSupportActionBarTitle(title: String?) { (activity as IToolbarTitleListener).updateTitle(title) }

}
