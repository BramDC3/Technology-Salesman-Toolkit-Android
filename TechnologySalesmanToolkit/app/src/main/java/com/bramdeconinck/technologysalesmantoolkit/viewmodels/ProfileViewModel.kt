package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel

class ProfileViewModel : InjectedViewModel() {

    private val _isEditable = MutableLiveData<Boolean>()
    val isEditable: LiveData<Boolean>
        get() = _isEditable

    init {
        _isEditable.value = false
    }

    fun toggleEditMode() { _isEditable.value = !_isEditable.value!! }

}