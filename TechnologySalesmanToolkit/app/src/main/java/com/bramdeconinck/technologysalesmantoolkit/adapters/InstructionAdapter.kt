package com.bramdeconinck.technologysalesmantoolkit.adapters

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.bramdeconinck.technologysalesmantoolkit.fragments.ServiceInstructionFragment
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.utils.INSTRUCTION_ITEM

class InstructionAdapter(
        private val instructions: MutableLiveData<List<Instruction>>,
        fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(p0: Int): Fragment {
        val item = instructions.value!![p0]

        return ServiceInstructionFragment().apply {
            arguments = Bundle().apply {
                putParcelable(INSTRUCTION_ITEM, item)
            }
        }
    }

    override fun getCount() = instructions.value!!.size
}