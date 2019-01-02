package com.bramdeconinck.technologysalesmantoolkit.adapters

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.bramdeconinck.technologysalesmantoolkit.fragments.ServiceInstructionFragment
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.utils.INSTRUCTION_ITEM

/**
 * [InstructionAdapter] is a [FragmentStatePagerAdapter] used for the ViewPager of the ServiceDetailFragment.
 */
class InstructionAdapter(
        private val instructions: LiveData<List<Instruction>>,
        fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

    /**
     * Function that fetches an Instruction.
     *
     * @return [ServiceInstructionFragment]: Returns a [ServiceInstructionFragment] for the ViewPager with the id of the [Instruction] as a parameter.
     */
    override fun getItem(p0: Int): Fragment {
        val item = instructions.value!![p0]

        return ServiceInstructionFragment().apply { arguments = Bundle().apply { putString(INSTRUCTION_ITEM, item.id) } }
    }

    override fun getCount() = instructions.value!!.size
}