package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import androidx.navigation.fragment.findNavController
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.databinding.FragmentProfileBinding
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IToastMaker
import com.bramdeconinck.technologysalesmantoolkit.utils.BaseCommand
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.firebaseUser
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showBasicDialog
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.showThreeButtonsPositiveFuncDialog
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.getFamilyName
import com.bramdeconinck.technologysalesmantoolkit.utils.StringUtils.getFirstName
import com.bramdeconinck.technologysalesmantoolkit.viewmodels.ProfileViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), IToastMaker {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
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

        updateUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if (profileViewModel.isEditable.value!!) profileViewModel.toggleEditMode()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_edit_profile, menu)
        menuItem = menu!!.findItem(R.id.action_edit_profile)
        initObservers()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return (when(item?.itemId) {
            R.id.action_edit_profile -> {
                profileViewModel.toggleEditMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        })
    }

    // Function to update the UI with data of the current FirebaseUser
    private fun updateUI() {
        Glide.with(this)
                .load(firebaseUser!!.photoUrl ?: R.drawable.default_profile_image)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile_profile_picture)

        tv_profile_fullname.text = firebaseUser!!.displayName
        et_profile_firstname.setText(getFirstName(firebaseUser!!.displayName!!))
        et_profile_familyname.setText(getFamilyName(firebaseUser!!.displayName!!))
        et_profile_email.setText(firebaseUser!!.email)
    }

    private fun initObservers() {
        profileViewModel.isEditable.observe(this, Observer {
            if (it!!) {
                menuItem.icon = context!!.getDrawable(R.drawable.ic_close_black_24dp)
                menuItem.title = getString(R.string.title_action_stop_editing_profile)
            } else {
                menuItem.icon = context!!.getDrawable(R.drawable.ic_edit_black_24dp)
                menuItem.title = getString(R.string.title_action_edit_profile)
                updateUI()
            }
        })

        profileViewModel.profileEditFormValidation.observe(this, Observer {
            when (it) {
                is BaseCommand.Error -> showToast(it.error!!)
                is BaseCommand.Success -> showThreeButtonsPositiveFuncDialog(
                        context!!,
                        getString(R.string.title_change_profile),
                        getString(R.string.message_change_profile),
                        profileViewModel.applyProfileChanges(
                                et_profile_firstname.text.toString(),
                                et_profile_familyname.text.toString(),
                                et_profile_email.text.toString()
                        )
                )
            }
        })

        profileViewModel.appliedNameChanges.observe(this, Observer {
            when (it) {
                is BaseCommand.Success -> {
                    showDialog(R.string.title_change_name, it.message!!)
                    updateUI()
                }
                is BaseCommand.Error -> { showDialog(R.string.title_change_name, it.error!!) }
            }
        })

        profileViewModel.appliedEmailChanges.observe(this, Observer {
            when (it) {
                is BaseCommand.Success -> {
                    showDialog(R.string.title_change_email, it.message!!)
                    this.findNavController().navigate(R.id.signOutFromProfile)
                }
                is BaseCommand.Error -> { showDialog(R.string.title_change_email, it.error!!) }
            }
        })

        profileViewModel.resetPasswordButtonClicked.observe(this, Observer {
            showThreeButtonsPositiveFuncDialog(
                    context!!,
                    getString(R.string.title_change_password),
                    getString(R.string.message_change_password),
                    profileViewModel.sendResetPasswordEmail()
            )
        })

        profileViewModel.requestedPasswordReset.observe(this, Observer {
            when (it) {
                is BaseCommand.Success -> { showDialog(R.string.title_change_password, it.message!!) }
                is BaseCommand.Error -> { showDialog(R.string.title_change_password, it.error!!) }
            }
        })

        profileViewModel.profilePictureClicked.observe(this, Observer {
            showDialog(
                    R.string.title_change_profile_picture,
                    R.string.message_change_profile_picture
            )
        })

        profileViewModel.editProfileButtonClicked.observe(this, Observer {
            profileViewModel.validateProfileForm(
                    et_profile_firstname.text.toString(),
                    et_profile_familyname.text.toString(),
                    et_profile_email.text.toString()
            )
        })
    }

    private fun showDialog(title: Int, message: Int) { showBasicDialog(context!!, getString(title), getString(message)) }

    override fun showToast(message: Int) { makeToast(context!!, message) }

}
