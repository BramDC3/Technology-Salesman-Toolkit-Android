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
import com.bramdeconinck.technologysalesmantoolkit.utils.SERVICE_ITEM
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.ServiceViewModel
import kotlinx.android.synthetic.main.fragment_service_detail.view.*

class ServiceDetailFragment : Fragment(), IToastMaker {

    private var service: Service? = null
    private lateinit var serviceViewModel: ServiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = arguments?.getParcelable(SERVICE_ITEM) as Service
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_service_detail, container, false)

        serviceViewModel = ViewModelProviders.of(activity!!).get(ServiceViewModel::class.java)

        serviceViewModel.fetchInstructions(service!!.id)

        val instructions = serviceViewModel.instructions

        val pagerAdapter = InstructionAdapter(instructions, childFragmentManager)

        rootView.vp_service_detail_instructions.adapter = pagerAdapter

        instructions.observe(this, Observer { pagerAdapter.notifyDataSetChanged() })

        serviceViewModel.roomInstructions.observe(this, Observer { serviceViewModel.onDatabaseInstructionsReady(service!!.id) })

        serviceViewModel.instructionsErrorOccurred.observe(this, Observer {
            showToast(R.string.fetching_instructions_error)
            serviceViewModel.onDatabaseInstructionsReady(service!!.id)
        })

        return rootView
    }

    override fun onStart() {
        super.onStart()

        setSupportActionBarTitle(service?.name)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        serviceViewModel.clearInstructions()
    }

    private fun setSupportActionBarTitle(title: String?) { (activity as IToolbarTitleListener).updateTitle(title) }

    override fun showToast(message: Int) { MessageUtils.makeToast(context!!, message)  }

}
