package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import android.webkit.WebViewClient
import com.bramdeconinck.technologysalesmantoolkit.R
import kotlinx.android.synthetic.main.service_detail.view.*

class ServiceDetailFragment : Fragment() {

    private var service: Service? = null
    private val defaultUrl: String = "https://bramdeconinck.com/404"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = arguments?.getParcelable(ARG_ITEM_ID) as Service
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.service_detail, container, false)

        loadWebpage(rootView.service_detail)

        return rootView
    }

    private fun loadWebpage(webview: WebView) {
        webview.loadUrl(defaultUrl)
        try {
            webview.loadUrl(service?.url)
            webview.webViewClient = WebViewClient()
            val webSettings = webview.settings
            webSettings.setAppCacheEnabled(true)
        } catch(e: UnsupportedOperationException) {
            e.printStackTrace()
        }
    }

    companion object {
         // The fragment argument representing the service that this fragment represents
        const val ARG_ITEM_ID = "serviceItem"
    }
}
