package com.bramdeconinck.technologysalesmantoolkit.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.bramdeconinck.technologysalesmantoolkit.R

object WebpageUtils {

    @JvmStatic
    fun openWebPage(url: String, context: Context) {
        try {
            val webpage = Uri.parse(url)
            val myIntent = Intent(Intent.ACTION_VIEW, webpage)
            context.startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            MessageUtils.makeToast(context, context.getString(R.string.error_no_browser_detected))
            e.printStackTrace()
        }
    }

}