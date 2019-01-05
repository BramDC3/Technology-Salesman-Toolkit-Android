package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getDrawable
import android.view.*
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.databinding.FragmentProfileBinding
import com.bramdeconinck.technologysalesmantoolkit.interfaces.ToastMaker
import com.bramdeconinck.technologysalesmantoolkit.utils.BaseCommand
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showBasicDialog
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showThreeButtonsPositiveFunctionDialog
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.getFamilyname
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.getFirstname
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.ProfileViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * [ProfileFragment] is a [Fragment] where users view their user details and change them.
 */
class ProfileFragment : Fragment(), ToastMaker {

    /**
     * [profileViewModel] contains all data and functions that have to do with the profile of the [firebaseUser].
     */
    private lateinit var profileViewModel: ProfileViewModel

    /**
     * [binding] is used for data binding.
     */
    private lateinit var binding: FragmentProfileBinding

    /**
     * [menuItem] is the menu item used for entering and exiting editing mode.
     * It is used to change the icon and and tooltip of the action bar icon
     */
    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        profileViewModel = ViewModelProviders.of(activity!!).get(ProfileViewModel::class.java)

        val rootView = binding.root
        binding.profileViewModel = profileViewModel
        binding.setLifecycleOwner(activity)

        return rootView
    }

    override fun onStart() {
        super.onStart()

        subscribeToObservables()

        updateUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        profileViewModel.exitEditingMode()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_edit_profile, menu)
        menuItem = menu!!.findItem(R.id.action_edit_profile)

        /**
         * Normally this function would have been in [subscribeToObservables],
         * but we need the menuItem to be initialized first, which happens later in the lifecycle.
         */
        profileViewModel.isEditable.observe(this, Observer { toggleEditMode(it!!) })

        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Inflating a menu with the menu item used to enter and exit editing mode.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return (when(item?.itemId) {
            R.id.action_edit_profile -> {
                profileViewModel.toggleEditMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        })
    }

    /**
     * Function to update the UI with data of the current [firebaseUser].
     */
    private fun updateUI() {
        /**
         * [Glide] is used to load the image of the [firebaseUser] into the ImageView.
         */
        Glide.with(this)
                .load(firebaseUser!!.photoUrl ?: R.drawable.default_profile_picture)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile_profile_picture)

        tv_profile_fullname.text = firebaseUser!!.displayName
        et_profile_firstname.setText(getFirstname(firebaseUser!!.displayName!!))
        et_profile_familyname.setText(getFamilyname(firebaseUser!!.displayName!!))
        et_profile_email.setText(firebaseUser!!.email)
    }

    /**
     * Function to subscribe to the observables of the [ProfileViewModel].
     */
    private fun subscribeToObservables() {
        profileViewModel.profileEditFormValidation.observe(this, Observer {
            when (it) {
                is BaseCommand.Success -> showChangeProfileDialog()
                is BaseCommand.Error -> showToast(it.error!!)
            }
        })

        profileViewModel.appliedNameChanges.observe(this, Observer {
            when (it) {
                is BaseCommand.Success -> { applyNameChanges(it.message!! )}
                is BaseCommand.Error -> { showDialog(R.string.title_change_name, it.error!!) }
            }
        })

        profileViewModel.appliedEmailChanges.observe(this, Observer {
            when (it) {
                is BaseCommand.Success -> { applyEmailChanges(it.message!!) }
                is BaseCommand.Error -> { showDialog(R.string.title_change_email, it.error!!) }
            }
        })

        profileViewModel.resetPasswordButtonClicked.observe(this, Observer { showResetPasswordDialog() })

        profileViewModel.profileEventOccurred.observe(this, Observer { showToast(it!!) })
    }

    /**
     * Function to change the icon and tooltip of the [menuItem],
     * based on whether the [ProfileFragment] is in editing mode or not.
     *
     * @param [isEditable]: Indicates whether the user is entering or exiting editing mode.
     */
    private fun toggleEditMode(isEditable: Boolean) {
        if (isEditable) {
            menuItem.icon = getDrawable(activity!!, R.drawable.ic_close_black_24dp)
            menuItem.title = getString(R.string.title_action_stop_editing_profile)
        } else {
            menuItem.icon = getDrawable(activity!!, R.drawable.ic_edit_black_24dp)
            menuItem.title = getString(R.string.title_action_edit_profile)
            updateUI()
        }
    }

    /**
     * Function for displaying a dialog where users can choose whether to apply their profile changes or not.
     */
    private fun showChangeProfileDialog() {
        showThreeButtonsPositiveFunctionDialog(
                context!!,
                getString(R.string.title_change_profile),
                getString(R.string.message_change_profile),
                profileViewModel.applyProfileChanges()
        )
    }

    /**
     * Function for displaying a dialog mentioning that the name of the user has been changed.
     *
     * @param [message]: String resource Id
     */
    private fun applyNameChanges(message: Int) {
        showDialog(R.string.title_change_name, message)
        updateUI()
    }

    /**
     * Function for displaying a dialog mentioning that the email address of the user has been changed
     * and redirecting the user back to the [LoginFragment].
     *
     * @param [message]: String resource Id
     */
    private fun applyEmailChanges(message: Int) {
        showDialog(R.string.title_change_email, message)
        findNavController().navigate(R.id.signOutFromProfile)
    }

    /**
     * Function for displaying a dialog where users can choose whether they want to change their password or not.
     */
    private fun showResetPasswordDialog() {
        showThreeButtonsPositiveFunctionDialog(
                context!!,
                getString(R.string.title_change_password),
                getString(R.string.message_change_password),
                profileViewModel.sendResetPasswordEmail()
        )
    }

    /**
     * Function for showing a basic dialog.
     *
     * @param [title]: String resource Id for the title of the dialog.
     * @param [message]: String resource Id for the message of the dialog.
     */
    private fun showDialog(title: Int, message: Int) { showBasicDialog(context!!, getString(title), getString(message)) }

    /**
     * Function for showing a toast message.
     *
     * @param [message]: String resource Id.
     */
    override fun showToast(message: Int) { makeToast(context!!, message) }

}