package com.bramdeconinck.technologysalesmantoolkit.network

import android.content.ContentValues.TAG
import android.util.Log
import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class FirestoreAPI {

    //Access a Cloud Firestore instance
    private val db = FirebaseFirestore.getInstance()

    //Get all services from the Firestore
    fun getServicesFromFirestore(): MutableList<Service> {
        val servicesList: MutableList<Service> = mutableListOf()
        db.collection("Services").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            servicesList.add(fromSnapshotToService(doc))
                        }
                        Log.d(TAG, "Retrieved all service documents successfully")
                    } else {
                        Log.d(TAG, "Error getting service documents: ", task.exception)
                    }
                }
        return servicesList
    }

    //Converting the data of a snapshot to a Service object
    private fun fromSnapshotToService(snapshot: QueryDocumentSnapshot): Service {
        return Service(
                id = snapshot.id,
                name = snapshot.getString("name")!!,
                description = snapshot.getString("description")!!,
                category = fromIntToCategory(snapshot.getDouble("category")!!.toInt()),
                created = snapshot.getTimestamp("created")!!,
                price = snapshot.getDouble("price")!!,
                image = snapshot.getString("image")!!,
                url = snapshot.getString("url")!!
        )
    }

    //Retrieve the right category based on an integer
    private fun fromIntToCategory(i: Int): Category {
        return when (i) {
            0 -> Category.Windows
            1 -> Category.Android
            2 -> Category.Apple
            else -> Category.Other
        }
    }

}