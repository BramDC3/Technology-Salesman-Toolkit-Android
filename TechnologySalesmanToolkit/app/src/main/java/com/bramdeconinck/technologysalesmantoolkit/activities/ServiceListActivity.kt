package com.bramdeconinck.technologysalesmantoolkit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.adapters.ServiceAdapter
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseCallback
import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.activity_service_list.*
import kotlinx.android.synthetic.main.service_list.*

class ServiceListActivity : AppCompatActivity(), IFirebaseCallback {

    private lateinit var firestoreApi: FirestoreAPI
    private lateinit var serviceData: MutableList<Service>
    private lateinit var serviceAdapter: ServiceAdapter

    //Variable to check whether the app is running on a tablet or not
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        //If the service detail container is not null,
        //then the app is opened with a tablet
        if (service_detail_container != null) {
            twoPane = true
        }

        firestoreApi = FirestoreAPI()
        serviceData = mutableListOf()
        serviceAdapter = ServiceAdapter(this, serviceData, twoPane)
        service_list.adapter = serviceAdapter
    }

    override fun onStart() {
        super.onStart()

        fillRecyclerview()
    }

    private fun fillRecyclerview() {
        if (serviceData.isNotEmpty()) serviceData.clear()
        firestoreApi.getServicesFromFirestore(this)
    }

    // De methodes van de interface IFirebaseCallback worden gebruikt
    // voor het ophalen van gegevens uit de Firebase en de
    // RecyclerView er mee te vullen.
    override fun onCallBack(list: MutableList<Service>) {
        serviceData.addAll(list)
        serviceAdapter.notifyDataSetChanged()
    }

    // Deze methode laat een loading indicator zien
    override fun showProgress() {
        progress_bar.visibility = View.VISIBLE
    }

    // Deze methode haalt de loading indicator weg
    override fun hideProgress() {
        progress_bar.visibility = View.GONE
    }

    // Deze methode toont een foutmelding indien er geen gegevens opgehaald kunnen worden.
    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
