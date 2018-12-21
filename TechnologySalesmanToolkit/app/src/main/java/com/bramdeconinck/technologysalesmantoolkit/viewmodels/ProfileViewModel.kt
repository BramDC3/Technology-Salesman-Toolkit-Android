package com.bramdeconinck.technologysalesmantoolkit.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.base.InjectedViewModel
import com.bramdeconinck.technologysalesmantoolkit.utils.BaseCommand
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.createProfileUpdates
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseAuth
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.SingleLiveEvent
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.getFamilyName
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.getFirstName
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.atLeastOneFieldChanged
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isEmailValid

class ProfileViewModel : InjectedViewModel() {

    val firstname = MutableLiveData<String>()

    val familyname = MutableLiveData<String>()

    val email = MutableLiveData<String>()

    private val _isEditable = MutableLiveData<Boolean>()
    val isEditable: LiveData<Boolean>
        get() = _isEditable

    val profileEditFormValidation = SingleLiveEvent<BaseCommand>()

    val resetPasswordButtonClicked = SingleLiveEvent<Any>()

    val profilePictureClicked = SingleLiveEvent<Any>()

    val appliedEmailChanges = SingleLiveEvent<BaseCommand>()

    val appliedNameChanges = SingleLiveEvent<BaseCommand>()

    val profileEventOccured = SingleLiveEvent<Int>()

    init {
        firstname.value = ""
        familyname.value = ""
        email.value = ""
        _isEditable.value = false
    }

    fun changeProfilePicture() { profilePictureClicked.call() }

    fun resetPassword() { resetPasswordButtonClicked.call() }

    fun toggleEditMode() { _isEditable.value = !_isEditable.value!! }

    // Function to send an e-mail to the current FirebaseUser containing a link to change their password
    fun sendResetPasswordEmail(): () -> Unit = {
        firebaseAuth.sendPasswordResetEmail(firebaseUser!!.email!!)
                .addOnSuccessListener { profileEventOccured.value = R.string.change_password_succes }
                .addOnFailureListener { profileEventOccured.value = R.string.change_password_failure }
    }

    fun validateProfileForm() {
        val profileFormMap = mapOf(email.value!! to firebaseUser!!.email!!,
                firstname.value!! to getFirstName(firebaseUser!!.displayName!!),
                familyname.value!! to getFamilyName(firebaseUser!!.displayName!!))

        if (!atLeastOneFieldChanged(profileFormMap)) {
            toggleEditMode()
            return
        }

        if (!everyFieldHasValue(listOf(firstname.value!!, familyname.value!!, email.value!!))) {
            profileEditFormValidation.value = BaseCommand.Error(R.string.error_empty_fields)
            return
        }

        if (!isEmailValid(email.value!!)) {
            profileEditFormValidation.value = BaseCommand.Error(R.string.error_invalid_email)
            return
        }

        profileEditFormValidation.value = BaseCommand.Success(null)
    }

    fun applyProfileChanges() =  {
        if (firebaseUser!!.displayName != "$firstname $familyname") {
            firebaseUser!!.updateProfile(createProfileUpdates(firstname.value!!, familyname.value!!))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            appliedNameChanges.value = BaseCommand.Success(R.string.message_change_name)
                            if (_isEditable.value!!) toggleEditMode()
                        }
                    }
                    .addOnFailureListener { appliedNameChanges.value = BaseCommand.Error(R.string.error_change_name) }
        }

        if (firebaseUser!!.email != email.value!!) {
            firebaseUser!!.updateEmail(email.value!!)
                    .addOnCompleteListener { task1 ->
                        if (task1.isSuccessful) {
                            firebaseUser!!.sendEmailVerification()
                                    .addOnCompleteListener { task2 ->
                                        if (task2.isSuccessful) {
                                            firebaseAuth.signOut()
                                            appliedEmailChanges.value = BaseCommand.Success(R.string.message_change_email)
                                        }
                                    }
                        }
                    }
                    .addOnFailureListener { appliedEmailChanges.value = BaseCommand.Error(R.string.error_change_email) }
        }
    }

    fun clearProfileForm() {
        firstname.value = ""
        familyname.value = ""
        email.value = ""
    }

}