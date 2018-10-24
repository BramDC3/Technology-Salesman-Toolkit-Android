package com.bramdeconinck.technologysalesmantoolkit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.adapters.ServiceAdapter
import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.activity_service_list.*
import kotlinx.android.synthetic.main.service_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ServiceDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ServiceListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private lateinit var firestoreApi: FirestoreAPI
    private lateinit var serviceData: MutableList<Service>
    private lateinit var serviceAdapter: ServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (service_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        firestoreApi = FirestoreAPI()
        serviceData = mutableListOf()
        serviceAdapter = ServiceAdapter(this, serviceData, twoPane)
        service_list.adapter = serviceAdapter
    }

    override fun onStart() {
        super.onStart()

        firestoreApi.getServicesFromFirestore(serviceData, serviceAdapter)
    }

}
