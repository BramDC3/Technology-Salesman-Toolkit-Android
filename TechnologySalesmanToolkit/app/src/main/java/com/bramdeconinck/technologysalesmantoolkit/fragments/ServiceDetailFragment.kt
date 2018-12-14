package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.adapters.InstructionAdapter
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IToastMaker
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IToolbarTitleListener
import com.bramdeconinck.technologysalesmantoolkit.utils.ARG_ITEM_ID
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.ServiceViewModel
import kotlinx.android.synthetic.main.fragment_service_detail.view.*

class ServiceDetailFragment : Fragment(), IToastMaker {

    private var service: Service? = null
    private lateinit var serviceViewModel: ServiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = arguments?.getParcelable(ARG_ITEM_ID) as Service
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_service_detail, container, false)

        serviceViewModel = ViewModelProviders.of(activity!!).get(ServiceViewModel::class.java)

        serviceViewModel.fetchInstructions(service!!.id)

        val instructions = serviceViewModel.getInstructions()

        val pagerAdapter = InstructionAdapter(instructions, childFragmentManager)

        rootView.vp_service_detail_instructions.adapter = pagerAdapter

        instructions.observe(this, Observer { pagerAdapter.notifyDataSetChanged() })

        serviceViewModel.instructionsErrorOccurred.observe(this, Observer { showToast(R.string.fetching_data_error) })

        return rootView
    }

    override fun onStart() {
        super.onStart()

        setSupportActionBarTitle(service?.name)
    }

    private fun setSupportActionBarTitle(title: String?) { (activity as IToolbarTitleListener).updateTitle(title) }

    override fun showToast(message: Int) { MessageUtils.makeToast(context!!, message)  }

}
