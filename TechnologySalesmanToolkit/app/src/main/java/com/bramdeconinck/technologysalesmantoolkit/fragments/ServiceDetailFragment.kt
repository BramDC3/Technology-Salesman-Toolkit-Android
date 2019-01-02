package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.LiveData
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

/**
 * [ServiceDetailFragment] is a [Fragment] where all instructions of a [Service] are shown in a ViewPager.
 */
class ServiceDetailFragment : Fragment(), ToastMaker {

    /**
     * [service] is the selected [Service].
     * [serviceViewModel] contains all data and functions that have to do with [Service] and [Instruction] objects.
     * [instructions] are the instructions of [service].
     * [pagerAdapter] is used to prepare [ServiceInstructionFragment] to be shown in a ViewPager.
     */
    private lateinit var service: Service
    private lateinit var serviceViewModel: ServiceViewModel
    private lateinit var instructions: LiveData<List<Instruction>>
    private lateinit var pagerAdapter: FragmentStatePagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_service_detail, container, false)

        serviceViewModel = ViewModelProviders.of(activity!!).get(ServiceViewModel::class.java)

        return rootView
    }

    override fun onStart() {
        super.onStart()

        prepareViewPager()

        subscribeToObservables()

        setSupportActionBarTitle(service.name)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        vp_service_detail_instructions.adapter = null

        serviceViewModel.clearInstructions()

        serviceViewModel.selectedService.value = null
    }

    /**
     * Function to get all the necessary data from the [ServiceViewModel],
     * and creates the [InstructionAdapter] used by the ViewPager.
     */
    private fun prepareViewPager() {
        service = serviceViewModel.selectedService.value!!

        serviceViewModel.fetchInstructions(service.id)

        instructions = serviceViewModel.instructions

        pagerAdapter = InstructionAdapter(instructions, childFragmentManager)

        vp_service_detail_instructions.adapter = pagerAdapter

        addViewPagerAnimation()
    }

    /**
     * Function to add an animation to the ViewPager.
     */
    private fun addViewPagerAnimation() {
        val transformer = BookFlipPageTransformer()
        transformer.isEnableScale = true
        transformer.scaleAmountPercent = 10f
        vp_service_detail_instructions.setPageTransformer(true, transformer)
    }

    /**
     * Function to subscribe to the observables of the [ServiceViewModel].
     */
    private fun subscribeToObservables() {
        instructions.observe(this, Observer { pagerAdapter.notifyDataSetChanged() })

        serviceViewModel.roomInstructions.observe(this, Observer { serviceViewModel.onDatabaseInstructionsReady(service.id) })

        serviceViewModel.instructionsErrorOccurred.observe(this, Observer {
            showToast(R.string.fetching_instructions_error)
            serviceViewModel.onDatabaseInstructionsReady(service.id)
        })
    }

    /**
     * Function to change the title of the action bar to the name of [service].
     */
    private fun setSupportActionBarTitle(title: String?) { (activity as ToolbarTitleChanger).updateTitle(title) }

    override fun showToast(message: Int) { MessageUtils.makeToast(context!!, message)  }

}