package com.bramdeconinck.technologysalesmantoolkit.network

import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseServiceCallback
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseSuggestionCallback
import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreSettings

class FirestoreAPI {

    // Access a Firebase Firestore instance
    private val firestore = FirebaseFirestore.getInstance()

    // These settings are used to hide a warning about dates and timestamps
    private val firetoreSettings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()

    init {
        firestore.firestoreSettings = firetoreSettings
    }

    // Get all services from the Firestore
    fun getServicesFromFirestore(firebaseServiceCallback: IFirebaseServiceCallback) {
        firebaseServiceCallback.showProgress()
        firestore.collection("Services").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val servicesList: MutableList<Service> = mutableListOf()
                        for (doc in task.result!!) {
                            servicesList.add(fromSnapshotToService(doc))
                        }
                        firebaseServiceCallback.onCallBack(servicesList)
                    }
                    else {
                        firebaseServiceCallback.showMessage()
                    }
                    firebaseServiceCallback.hideProgress()
                }
    }

    // Converting the data of a snapshot to a Service object
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

    // Retrieve the right category of a service based on an integer
    private fun fromIntToCategory(i: Int): Category {
        return when (i) {
            0 -> Category.Windows
            1 -> Category.Android
            2 -> Category.Apple
            else -> Category.Other
        }
    }

    // Post a suggestion to the Firestore
    fun postSuggestiontoFirestore(callback: IFirebaseSuggestionCallback, suggestion: String) {
        val data = HashMap<String, Any>()

        data["message"] = suggestion
        data["sender"] = FirebaseAuth.getInstance().currentUser?.uid ?: "Anonymous"

        firestore.collection("Suggestions").add(data)
                .addOnSuccessListener { callback.showSuccesMessage() }
                .addOnFailureListener { callback.showFailureMessage() }
    }

}