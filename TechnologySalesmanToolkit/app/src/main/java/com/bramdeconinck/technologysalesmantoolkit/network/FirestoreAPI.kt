package com.bramdeconinck.technologysalesmantoolkit.network

import android.content.ContentValues.TAG
import android.util.Log
import com.bramdeconinck.technologysalesmantoolkit.adapters.ServiceAdapter
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseCallback
import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class FirestoreAPI {

    //Access a Cloud Firestore instance
    private val db = FirebaseFirestore.getInstance()

    //Get all services from the Firestore
    fun getServicesFromFirestore(firebaseCallback: IFirebaseCallback) {
        firebaseCallback.showProgress()
        val servicesList: MutableList<Service> = mutableListOf()
        db.collection("Services").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result!!) {
                            servicesList.add(fromSnapshotToService(doc))
                        }
                        firebaseCallback.onCallBack(servicesList)
                        Log.d(TAG, "Retrieved all service documents successfully")
                    } else {
                        firebaseCallback.showMessage("Er is een fout opgetreden tijdens het ophalen van de gegevens.")
                        Log.d(TAG, "Error getting service documents: ", task.exception)
                    }
                    firebaseCallback.hideProgress()
                }
    }

    //Converting the data of a snapshot to a Service object
    private fun fromSnapshotToService(snapshot: QueryDocumentSnapshot): Service {
        Log.d("TAG", "Service gevonden: ${snapshot.id}")

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