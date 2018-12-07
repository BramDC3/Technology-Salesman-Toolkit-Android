package com.bramdeconinck.technologysalesmantoolkit.utils

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.activities.MainActivity
import com.bramdeconinck.technologysalesmantoolkit.utils.MessageUtils.makeToast

object WebpageUtils {

    @JvmStatic
    fun openWebPage(url: String) {
        try {
            val webpage = Uri.parse(url)
            val myIntent = Intent(Intent.ACTION_VIEW, webpage)
            MainActivity.getContext().startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            makeToast(R.string.error_no_browser_detected)
            e.printStackTrace()
        }
    }

}