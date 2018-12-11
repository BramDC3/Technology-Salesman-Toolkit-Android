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

    private val _isEditable = MutableLiveData<Boolean>()
    val isEditable: LiveData<Boolean>
        get() = _isEditable

    val profileEditFormValidation = SingleLiveEvent<BaseCommand>()

    val appliedNameChanges = SingleLiveEvent<BaseCommand>()

    val appliedEmailChanges = SingleLiveEvent<BaseCommand>()

    val editProfileButtonClicked = SingleLiveEvent<Any>()

    val resetPasswordButtonClicked = SingleLiveEvent<Any>()

    val profilePictureClicked = SingleLiveEvent<Any>()

    val requestedPasswordReset = SingleLiveEvent<BaseCommand>()

    init {
        _isEditable.value = false
    }

    fun editProfile() { editProfileButtonClicked.call() }

    fun changeProfilePicture() { profilePictureClicked.call() }

    fun resetPassword() { resetPasswordButtonClicked.call() }

    fun toggleEditMode() { _isEditable.value = !_isEditable.value!! }

    // Function to send an e-mail to the current FirebaseUser containing a link to change their password
    fun sendResetPasswordEmail(): () -> Unit = {
        firebaseAuth.sendPasswordResetEmail(firebaseUser!!.email!!)
                .addOnSuccessListener { requestedPasswordReset.value = BaseCommand.Success(R.string.change_password_succes) }
                .addOnFailureListener { requestedPasswordReset.value = BaseCommand.Success(R.string.change_password_failure) }
    }

    fun validateProfileForm(firstname: String, familyname: String, email: String) {
        val profileFormMap = mapOf(email to firebaseUser!!.email!!,
                firstname to getFirstName(firebaseUser!!.displayName!!),
                familyname to getFamilyName(firebaseUser!!.displayName!!))

        if (!atLeastOneFieldChanged(profileFormMap)) {
            toggleEditMode()
            return
        }

        if (!everyFieldHasValue(listOf(firstname, familyname, email))) {
            profileEditFormValidation.value = BaseCommand.Error(R.string.error_empty_fields)
            return
        }

        if (!isEmailValid(email)) {
            profileEditFormValidation.value = BaseCommand.Error(R.string.error_invalid_email)
            return
        }

        profileEditFormValidation.value = BaseCommand.Success(null)
    }

    fun applyProfileChanges(firstname: String, familyname: String, email: String) =  {
        if (firebaseUser!!.displayName != "$firstname $familyname") {
            firebaseUser!!.updateProfile(createProfileUpdates(firstname, familyname))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            appliedNameChanges.value = BaseCommand.Success(R.string.message_change_name)
                            if (_isEditable.value!!) toggleEditMode()
                        }
                    }
                    .addOnFailureListener { appliedNameChanges.value = BaseCommand.Error(R.string.error_change_name) }
        }

        if (firebaseUser!!.email != email) {
            firebaseUser!!.updateEmail(email)
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

}