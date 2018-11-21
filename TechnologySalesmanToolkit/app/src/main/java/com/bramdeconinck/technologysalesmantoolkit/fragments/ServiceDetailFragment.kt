package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import android.webkit.WebViewClient
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IToolbarTitleListener
import kotlinx.android.synthetic.main.fragment_service_detail.view.*

class ServiceDetailFragment : Fragment() {

    private var service: Service? = null
    private val defaultUrl: String = "https://bramdeconinck.com/404"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = arguments?.getParcelable(ARG_ITEM_ID) as Service
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_service_detail, container, false)

        loadWebpage(rootView.service_detail)

        return rootView
    }

    override fun onStart() {
        super.onStart()

        setSupportActionBarTitle(service?.name)
    }

    private fun setSupportActionBarTitle(title: String?) {
        (activity as IToolbarTitleListener).updateTitle(title)
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
