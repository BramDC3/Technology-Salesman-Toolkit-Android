package com.bramdeconinck.technologysalesmantoolkit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.fragments.ServiceDetailFragment
import com.bramdeconinck.technologysalesmantoolkit.models.Service

class ServiceDetailActivity : AppCompatActivity() {

    private var service: Service? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)

        service = intent.getParcelableExtra(ServiceDetailFragment.ARG_ITEM_ID)

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity using a fragment transaction.
            val fragment = ServiceDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ServiceDetailFragment.ARG_ITEM_ID, service)
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.service_detail_container, fragment)
                    .commit()
        }
    }

}
