package com.bramdeconinck.technologysalesmantoolkit.utils

import android.app.AlertDialog
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.activities.MainActivity
import com.bramdeconinck.technologysalesmantoolkit.utils.WebpageUtils.openWebPage

object MessageUtils {

    @JvmStatic
    // Shows a toast on the screen for a chosen amount of time with a given stringResourceId
    fun makeToast(stringResourceId: Int, message: String? = null, duration: Int = Toast.LENGTH_LONG) {
        if (message == null) Toast.makeText(MainActivity.getContext(), stringResourceId, duration).show()
        else Toast.makeText(MainActivity.getContext(), MainActivity.getContext().getString(stringResourceId, message), duration).show()
    }

    @JvmStatic
    // Show a dialog on the screen with a given title and message
    fun showBasicDialog(title: String, message: String) {
        AlertDialog.Builder(MainActivity.getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Oké") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

    @JvmStatic
    fun showThreeButtonsPositiveFuncDialog(title: String, message: String, func: () -> Unit) {
        AlertDialog.Builder(MainActivity.getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ja") { _, _ -> func() }
                .setNegativeButton("Nee") { dialog, _ -> dialog.dismiss() }
                .setNeutralButton("Annuleren") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

    @JvmStatic
    fun showMakeSuggestionDialog(title: String, message: String, func: (String) -> Unit) {
        val editText = EditText(MainActivity.getContext())
        editText.setSingleLine(false)
        val container = FrameLayout(MainActivity.getContext())
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.marginStart = MainActivity.getContext().resources.getDimensionPixelSize(R.dimen.dialog_margin)
        params.marginEnd = MainActivity.getContext().resources.getDimensionPixelSize(R.dimen.dialog_margin)
        editText.layoutParams = params
        container.addView(editText)

        AlertDialog.Builder(MainActivity.getContext())
                .setTitle(title)
                .setView(container)
                .setMessage(message)
                .setPositiveButton("Verzend") { _, _ -> func(editText.text.toString()) }
                .setNeutralButton("Annuleren") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

    @JvmStatic
    // Function to show the dialog which asks the user if they want to change their password
    fun showEditProfileDialog(title: String, message: String, funcPositive: () -> Unit, funcNegative: () -> Unit) {
        AlertDialog.Builder(MainActivity.getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ja") { _, _ -> funcPositive()}
                .setNegativeButton("Nee") { _, _ -> funcNegative() }
                .setNeutralButton("Annuleren") { _, _ -> funcNegative() }
                .create()
                .show()
    }

    @JvmStatic
    fun showPrivacyPolicyDialog(title: String, message: String, func: () -> Unit) {
        AlertDialog.Builder(MainActivity.getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ja") { _, _ -> func() }
                .setNegativeButton("Nee") { dialog, _ -> dialog.dismiss() }
                .setNeutralButton("Bekijk privacybeleid") { _, _ -> openWebPage(privacyPolicy) }
                .create()
                .show()
    }

}