package com.bramdeconinck.technologysalesmantoolkit.fragments


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.utils.INSTRUCTION_ITEM
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.formatInstructionsList
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.ServiceViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_service_instruction.*

/**
 * [ServiceInstructionFragment] is a [Fragment] used for displaying a single [Instruction] object.
 */
class ServiceInstructionFragment : Fragment() {

    /**
     * [instruction] is the selected [Instruction].
     * [serviceViewModel] contains all data and functions that have to do with [Service] and [Instruction] objects.
     */
    private lateinit var instruction: Instruction
    private lateinit var serviceViewModel: ServiceViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        serviceViewModel = ViewModelProviders.of(activity!!).get(ServiceViewModel::class.java)

        /**
         * Getting the selected instruction out of the navigation arguments.
         */
        val instructionId = arguments!!.getString(INSTRUCTION_ITEM)!!

        instruction = serviceViewModel.getInstructionById(instructionId)

        return inflater.inflate(R.layout.fragment_service_instruction, container, false)
    }

    override fun onStart() {
        super.onStart()

        updateUI()
    }

    /**
     * Function to update the UI with data of [instruction].
     */
    private fun updateUI() {
        Glide.with(this)
                .load(instruction.image)
                .into(iv_service_instruction_image)

        tv_service_instruction_title.text = instruction.title
        tv_service_instruction_description.text = instruction.description
        tv_service_instruction_content.text = formatInstructionsList(instruction.content)
    }

}