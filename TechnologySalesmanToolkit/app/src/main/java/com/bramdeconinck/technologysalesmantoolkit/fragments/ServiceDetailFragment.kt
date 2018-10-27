package com.bramdeconinck.technologysalesmantoolkit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_service_detail.*
import kotlinx.android.synthetic.main.service_detail.view.*

class ServiceDetailFragment : Fragment() {

    private var item: DummyContent.DummyItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = DummyContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.service_detail, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            rootView.service_detail.text = it.details
        }

        return rootView
    }

    companion object {
         // The fragment argument representing the item ID that this fragment represents.
        const val ARG_ITEM_ID = "item_id"
    }
}
