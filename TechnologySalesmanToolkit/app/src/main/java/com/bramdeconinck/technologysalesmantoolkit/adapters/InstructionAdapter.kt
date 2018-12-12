package com.bramdeconinck.technologysalesmantoolkit.adapters

import android.arch.lifecycle.MutableLiveData
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction

class InstructionAdapter(
        private val instructions: MutableLiveData<List<Instruction>>,
        private val fragmentManager: FragmentManager
): FragmentPagerAdapter(fragmentManager) {

    override fun getItem(p0: Int): Fragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount() = instructions.value!!.size
}