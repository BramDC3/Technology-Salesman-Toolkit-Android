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
     * [firstname] is the first name of the FirebaseUser that is currently signed in.
     */
    val firstname = MutableLiveData<String>()

    /**
     * [familyname] is the family name of the FirebaseUser that is currently signed in.
     */
    val familyname = MutableLiveData<String>()

    /**
     * [email] is the email address of the FirebaseUser that is currently signed in.
     */
    val email = MutableLiveData<String>()

    /**
     * Value that indicates whether editing mode is enabled or not.
     */
    private val _isEditable = MutableLiveData<Boolean>()
    val isEditable: LiveData<Boolean>
        get() = _isEditable

    /**
     * [SingleLiveEvent] that indicates an event occurred when validating the profile form.
     * It has the value of a [BaseCommand].
     */
    val profileEditFormValidation = SingleLiveEvent<BaseCommand>()

    /**
     * [SingleLiveEvent] that indicates the user wants to change his/her password.
     */
    val resetPasswordButtonClicked = SingleLiveEvent<Any>()

    /**
     * [SingleLiveEvent] that indicates an event occurred when changing the email address of the user.
     * It has the value of a [BaseCommand].
     */
    val appliedEmailChanges = SingleLiveEvent<BaseCommand>()

    /**
     * [SingleLiveEvent] that indicates an event occurred when changing the name of the user.
     * It has the value of a [BaseCommand].
     */
    val appliedNameChanges = SingleLiveEvent<BaseCommand>()

    /**
     * [SingleLiveEvent] that indicates an event for which a message should be displayed has occurred.
     * It has the value of a String resource Id.
     */
    val profileEventOccurred = SingleLiveEvent<Int>()

    init {
        firstname.value = ""
        familyname.value = ""
        email.value = ""
        _isEditable.value = false
    }

    /**
     * Function that gets called when the Change Password TextView is clicked.
     */
    fun resetPassword() { resetPasswordButtonClicked.call() }

    /**
     * Function that enters or exits editing mode.
     */
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
     * Function that gets called when the user changed their profile and wants to save the changes.
     * It checks if the name, the email address or both have to be changed.
     */
    fun applyProfileChanges() =  {
        if (firebaseUser!!.displayName != "${firstname.value} ${familyname.value}") applyNameChanges()

        if (firebaseUser!!.email != email.value!!) applyEmailChanges()
    }

    /**
     * Function to apply the changes to the name the user has made.
     */
    private fun applyNameChanges() {
        firebaseUser!!.updateProfile(createProfileUpdates(firstname.value!!, familyname.value!!))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        appliedNameChanges.value = BaseCommand.Success(R.string.message_change_name)
                        if (_isEditable.value!!) toggleEditMode()
                    }
                }
                .addOnFailureListener { appliedNameChanges.value = BaseCommand.Error(R.string.error_change_name) }
    }

    /**
     * Function to apply the changes to the email address the user has made.
     */
    private fun applyEmailChanges() {
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

    /**
     * Function to exit the editing mode if it was entered.
     */
    fun exitEditingMode() { if (isEditable.value!!) toggleEditMode() }

}