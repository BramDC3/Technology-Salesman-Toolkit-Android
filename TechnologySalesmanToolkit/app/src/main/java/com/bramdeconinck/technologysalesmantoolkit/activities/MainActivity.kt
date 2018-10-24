package com.bramdeconinck.technologysalesmantoolkit.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bramdeconinck.technologysalesmantoolkit.R
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI

class MainActivity : AppCompatActivity() {

    private lateinit var firestoreApi: FirestoreAPI
    //private lateinit var services: MutableList<Service>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firestoreApi = FirestoreAPI()
    }

    override fun onStart() {
        super.onStart()

        //services = firestoreApi.getServicesFromFirestore()
    }

}
