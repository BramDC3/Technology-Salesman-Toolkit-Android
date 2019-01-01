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
    // Shows a toast on the screen for a chosen amount of time with a given stringResourceId
    fun makeToast(context: Context, stringResourceId: Int, message: String? = null, duration: Int = Toast.LENGTH_LONG) {
        if (message == null) Toast.makeText(context, stringResourceId, duration).show()
        else Toast.makeText(context, context.getString(stringResourceId, message), duration).show()
    }

    @JvmStatic
    // Show a dialog on the screen with a given title and message
    fun showBasicDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.dialog_okay)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

    @JvmStatic
    fun showThreeButtonsPositiveFunctionDialog(context: Context, title: String, message: String, func: () -> Unit) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.dialog_yes)) { _, _ -> func() }
                .setNegativeButton(context.getString(R.string.dialog_no)) { dialog, _ -> dialog.dismiss() }
                .setNeutralButton(context.getString(R.string.dialog_cancel)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

    @JvmStatic
    fun showMakeSuggestionDialog(context: Context, title: String, message: String, func: (String) -> Unit) {
        val editText = EditText(context)
        editText.setSingleLine(false)
        editText.hint = context.getString(R.string.send_suggestion_hint)
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
                .setPositiveButton(context.getString(R.string.dialog_send)) { _, _ -> func(editText.text.toString()) }
                .setNeutralButton(context.getString(R.string.dialog_cancel)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

    @JvmStatic
    fun showPrivacyPolicyDialog(context: Context, title: String, message: String, func: () -> Unit) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.dialog_yes)) { _, _ -> func() }
                .setNegativeButton(context.getString(R.string.dialog_no)) { dialog, _ -> dialog.dismiss() }
                .setNeutralButton(context.getString(R.string.dialog_privacy_policy)) { _, _ -> openWebPage(context, PRIVACY_POLICY) }
                .create()
                .show()
    }

}