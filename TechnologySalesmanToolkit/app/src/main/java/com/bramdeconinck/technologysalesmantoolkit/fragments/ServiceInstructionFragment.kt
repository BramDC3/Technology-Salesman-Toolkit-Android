package com.bramdeconinck.technologysalesmantoolkit.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.utils.INSTRUCTION_ITEM
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.createFormattedInstructionsList
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_service_instruction.*
import kotlinx.android.synthetic.main.fragment_service_instruction.view.*

class ServiceInstructionFragment : Fragment() {
    private var instruction: Instruction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        instruction = arguments?.getParcelable(INSTRUCTION_ITEM) as Instruction
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_service_instruction, container, false)
    }

    override fun onStart() {
        super.onStart()

        updateUI()
    }

    private fun updateUI() {
        Glide.with(this)
                .load(instruction!!.image)
                .into(iv_service_instruction_image)

        tv_service_instruction_title.text = instruction!!.title
        tv_service_instruction_description.text = instruction!!.description
        tv_service_instruction_content.text = createFormattedInstructionsList(instruction!!.content)
    }

}