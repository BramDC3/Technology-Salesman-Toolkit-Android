package com.bramdeconinck.technologysalesmantoolkit.utils

import android.app.AlertDialog
import android.content.Context
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.WebpageUtils.openWebPage

object MessageUtils {

    @JvmStatic
    // Shows a toast on the screen for a chosen amount of time with a given message
    fun makeToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) { Toast.makeText(context, message, duration).show() }

    @JvmStatic
    // Show a dialog on the screen with a given title and message
    fun showBasicDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Oké") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

    @JvmStatic
    fun showThreeButtonsPositiveFuncDialog(context: Context, title: String, message: String, func: () -> Unit) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ja") { _, _ -> func() }
                .setNegativeButton("Nee") { dialog, _ -> dialog.dismiss() }
                .setNeutralButton("Annuleren") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

    @JvmStatic
    fun showMakeSuggestionDialog(context: Context, title: String, message: String, func: (String) -> Unit) {
        val editText = EditText(context)
        editText.setSingleLine(false)
        val container = FrameLayout(context)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.marginStart = context.resources.getDimensionPixelSize(R.dimen.dialog_margin)
        params.marginEnd = context.resources.getDimensionPixelSize(R.dimen.dialog_margin)
        editText.layoutParams = params
        container.addView(editText)

        AlertDialog.Builder(context)
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
    fun showEditProfileDialog(context: Context, title: String, message: String, funcPositive: () -> Unit, funcNegative: () -> Unit) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ja") { _, _ -> funcPositive()}
                .setNegativeButton("Nee") { _, _ -> funcNegative() }
                .setNeutralButton("Annuleren") { _, _ -> funcNegative() }
                .create()
                .show()
    }

    @JvmStatic
    fun showPrivacyPolicyDialog(context: Context, title: String, message: String, func: () -> Unit) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ja") { _, _ -> func() }
                .setNegativeButton("Nee") { dialog, _ -> dialog.dismiss() }
                .setNeutralButton("Bekijk privacybeleid") { _, _ -> openWebPage(privacyPolicy, context) }
                .create()
                .show()
    }

}