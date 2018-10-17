package com.bramdeconinck.technologysalesmantoolkit.network

import android.content.ContentValues.TAG
import android.util.Log
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class FirestoreAPI {

    //Access a Cloud Firestore instance
    val db = FirebaseFirestore.getInstance()

    //Get all services from the Firestore
    fun getServicesFromFirestore(): MutableList<Service> {
        val servicesList: MutableList<Service> = mutableListOf()
        db.collection("Services").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            servicesList.add(fromJSONToService(doc))
                        }
                        Log.d(TAG, "Retrieved all service documents successfully")
                    } else {
                        Log.d(TAG, "Error getting service documents: ", task.exception)
                    }
                }
        return servicesList
    }

    //Converting JSON data to a Service object
    private fun fromJSONToService(snapshot: QueryDocumentSnapshot): Service {
        return Service(
                id = snapshot.id,
                name = snapshot.getString("name")!!,
                description = snapshot.getString("description")!!,
                category = snapshot.getDouble("category")!!.toInt(),
                created = snapshot.getTimestamp("created")!!,
                price = snapshot.getDouble("price")!!,
                image = snapshot.getString("image")!!,
                url = snapshot.getString("url")!!
        )
    }

}