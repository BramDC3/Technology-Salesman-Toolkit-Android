package com.bramdeconinck.technologysalesmantoolkit.network

import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseServiceCallback
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseSuggestionCallback
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.createSuggestionData
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.fromSnapshotToService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class FirestoreAPI {

    // Access a Firebase Firestore instance
    private val firestore = FirebaseFirestore.getInstance()

    init {
        // These settings are used to hide a warning about dates and timestamps
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()
    }

    // Get all services from the Firestore
    fun getAllServices(firebaseServiceCallback: IFirebaseServiceCallback) {
        firebaseServiceCallback.showProgress()
        firestore.collection("Services").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val servicesList: MutableList<Service> = mutableListOf()
                        for (doc in task.result!!) { servicesList.add(fromSnapshotToService(doc)) }
                        firebaseServiceCallback.onCallBack(servicesList)
                    }
                    else { firebaseServiceCallback.showMessage() }
                    firebaseServiceCallback.hideProgress()
                }
    }

    // Post a suggestion to the Firestore
    fun postSuggestion(callback: IFirebaseSuggestionCallback, suggestion: String) {
        firestore.collection("Suggestions").add(createSuggestionData(suggestion))
                .addOnSuccessListener { callback.showSuccesMessage() }
                .addOnFailureListener { callback.showFailureMessage() }
    }

}