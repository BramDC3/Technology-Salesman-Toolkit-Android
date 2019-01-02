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
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.getFamilyname
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.getFirstname
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.atLeastOneFieldChanged
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.everyFieldHasValue
import com.bramdeconinck.technologysalesmantoolkit.utils.ValidationUtils.isEmailValid

/**
 * Instance of [InjectedViewModel] that contains data and functions used in the ProfileFragment.
 */
class ProfileViewModel : InjectedViewModel() {

    /**
     * Values for fields of the profile form.
     */
    val firstname = MutableLiveData<String>()
    val familyname = MutableLiveData<String>()
    val email = MutableLiveData<String>()

    /**
     * Value that indicates whether editing mode is enabled or not.
     */
    private val _isEditable = MutableLiveData<Boolean>()
    val isEditable: LiveData<Boolean>
        get() = _isEditable

    /**
     * [SingleLiveEvent] objects used to emit events.
     */
    val profileEditFormValidation = SingleLiveEvent<BaseCommand>()
    val resetPasswordButtonClicked = SingleLiveEvent<Any>()
    val profilePictureClicked = SingleLiveEvent<Any>()
    val appliedEmailChanges = SingleLiveEvent<BaseCommand>()
    val appliedNameChanges = SingleLiveEvent<BaseCommand>()
    val profileEventOccurred = SingleLiveEvent<Int>()

    init {
        firstname.value = ""
        familyname.value = ""
        email.value = ""
        _isEditable.value = false
    }

    fun changeProfilePicture() { profilePictureClicked.call() }

    fun resetPassword() { resetPasswordButtonClicked.call() }

    fun toggleEditMode() { _isEditable.value = !_isEditable.value!! }

    /**
     * Function that sends an e-mail to the [firebaseUser] containing a link to change their password.
     */
    fun sendResetPasswordEmail(): () -> Unit = {
        firebaseAuth.sendPasswordResetEmail(firebaseUser!!.email!!)
                .addOnSuccessListener { profileEventOccurred.value = R.string.change_password_succes }
                .addOnFailureListener { profileEventOccurred.value = R.string.change_password_failure }
    }

    /**
     * Function to validate the profile form.
     */
    fun validateProfileForm() {
        val profileFormMap = mapOf(email.value!! to firebaseUser!!.email!!,
                firstname.value!! to getFirstname(firebaseUser!!.displayName!!),
                familyname.value!! to getFamilyname(firebaseUser!!.displayName!!))

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

    /**
     * Function that applies the profile changes.
     * It handles both changes to the name and the email address of the [firebaseUser].
     */
    fun applyProfileChanges() =  {
        if (firebaseUser!!.displayName != "${firstname.value} ${familyname.value}") {
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

    fun clearProfileForm() { if (isEditable.value!!) toggleEditMode() }

}