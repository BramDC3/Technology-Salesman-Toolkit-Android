package com.bramdeconinck.technologysalesmantoolkit.utils

import android.app.AlertDialog
import android.content.Context
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.WebpageUtils.openWebPage

/**
 * [MessageUtils] contains all the [Toast] and [AlertDialog] utilities.
 */
object MessageUtils {

    /**
     * Shows a toast on the screen for a chosen amount of time with a given stringResourceId.
     *
     * @param context: [Context] of the fragment that used this function.
     * @param stringResourceId: String resource Id.
     * @param message: Optional parameter that is needed for the string resource.
     * @param duration: How long the toast should be shown.
     */
    @JvmStatic
    fun makeToast(context: Context, stringResourceId: Int, message: String? = null, duration: Int = Toast.LENGTH_LONG) {
        if (message == null) Toast.makeText(context, stringResourceId, duration).show()
        else Toast.makeText(context, context.getString(stringResourceId, message), duration).show()
    }

    /**
     * Show a dialog on the screen with a given title and message.
     *
     * @param context: [Context] of the fragment that used this function.
     * @param title: Title of the dialog.
     * @param message: Message of the dialog.
     */
    @JvmStatic
    fun showBasicDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.dialog_okay)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }

    /**
     * Show a dialog on the screen with a given title and message.
     * The positive button has a given function.
     *
     * @param context: [Context] of the fragment that used this function.
     * @param title: Title of the dialog.
     * @param message: Message of the dialog.
     * @param func: Function that is called when the positive button is clicked.
     */
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

    /**
     * Show a dialog on the screen with a given title and message.
     * The dialog contains an [EditText] used to enter a suggestion.
     *
     * @param context: [Context] of the fragment that used this function.
     * @param title: Title of the dialog.
     * @param message: Message of the dialog.
     * @param func: Function that is called when the positive button is clicked.
     */
    @JvmStatic
    fun showMakeSuggestionDialog(context: Context, title: String, message: String, func: (String) -> Unit) {

        /**
         * Programmatically created [EditText] that is used as the body of the dialog.
         * The user can type their suggestion here and the function of the positive button uses the entered text.
         */
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

    /**
     * Show a dialog on the screen with a given title and message.
     * The positive button has a given function.
     * The neutral button opens the privacy policy via an intent.
     *
     * @param context: [Context] of the fragment that used this function.
     * @param title: Title of the dialog.
     * @param message: Message of the dialog.
     * @param func: Function that is called when the positive button is clicked.
     */
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