package com.bramdeconinck.technologysalesmantoolkit.adapters

import android.arch.lifecycle.MutableLiveData
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.bramdeconinck.technologysalesmantoolkit.fragments.ServiceInstructionFragment
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction

class InstructionAdapter(
        private val instructions: MutableLiveData<List<Instruction>>,
        fragmentManager: FragmentManager)
    : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(p0: Int): Fragment { return ServiceInstructionFragment() }

    override fun getCount() = instructions.value!!.size
}