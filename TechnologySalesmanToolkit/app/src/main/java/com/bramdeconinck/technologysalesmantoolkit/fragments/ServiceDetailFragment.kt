package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.adapters.InstructionAdapter
import com.bramdeconinck.technologysalesmantoolkit.interfaces.ToastMaker
import com.bramdeconinck.technologysalesmantoolkit.interfaces.ToolbarTitleChanger
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.ServiceViewModel
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer
import kotlinx.android.synthetic.main.fragment_service_detail.*
import kotlinx.android.synthetic.main.fragment_service_detail.view.*

class ServiceDetailFragment : Fragment(), ToastMaker {

    private lateinit var service: Service
    private lateinit var serviceViewModel: ServiceViewModel
    private lateinit var instructions: MutableLiveData<List<Instruction>>
    private lateinit var pagerAdapter: FragmentStatePagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_service_detail, container, false)

        serviceViewModel = ViewModelProviders.of(activity!!).get(ServiceViewModel::class.java)

        service = serviceViewModel.selectedService.value!!

        serviceViewModel.fetchInstructions(service.id)

        instructions = serviceViewModel.instructions

        pagerAdapter = InstructionAdapter(instructions, childFragmentManager)

        rootView.vp_service_detail_instructions.adapter = pagerAdapter

        // Add animation to viewpager
        val transformer = BookFlipPageTransformer()
        transformer.isEnableScale = true
        transformer.scaleAmountPercent = 10f
        rootView.vp_service_detail_instructions.setPageTransformer(true, transformer)

        return rootView
    }

    override fun onStart() {
        super.onStart()

        subscribeToObservables()

        setSupportActionBarTitle(service.name)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        vp_service_detail_instructions.adapter = null

        serviceViewModel.clearInstructions()

        serviceViewModel.selectedService.value = null
    }

    private fun subscribeToObservables() {
        instructions.observe(this, Observer { pagerAdapter.notifyDataSetChanged() })

        serviceViewModel.roomInstructions.observe(this, Observer { serviceViewModel.onDatabaseInstructionsReady(service.id) })

        serviceViewModel.instructionsErrorOccurred.observe(this, Observer {
            showToast(R.string.fetching_instructions_error)
            serviceViewModel.onDatabaseInstructionsReady(service.id)
        })
    }

    private fun setSupportActionBarTitle(title: String?) { (activity as ToolbarTitleChanger).updateTitle(title) }

    override fun showToast(message: Int) { MessageUtils.makeToast(context!!, message)  }

}