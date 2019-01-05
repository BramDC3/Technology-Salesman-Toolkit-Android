package com.bramdeconinck.technologysalesmantoolkit.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast

/**
 * [WebpageUtils] contains all the utilities for opening webpages.
 */
object WebpageUtils {

    /**
     * Open a webpage with a given url.
     *
     * @param context: [Context] of the fragment that used this function.
     * @param url: Link to the website that should be opened.
     */
    @JvmStatic
    fun openWebPage(context: Context, url: String) {
        try {
            val webpage = Uri.parse(url)
            val myIntent = Intent(Intent.ACTION_VIEW, webpage)
            context.startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            makeToast(context, R.string.error_no_browser_detected)
            e.printStackTrace()
        }
    }

}