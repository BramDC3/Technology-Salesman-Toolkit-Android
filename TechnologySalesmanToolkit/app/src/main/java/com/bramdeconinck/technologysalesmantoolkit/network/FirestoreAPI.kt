package com.bramdeconinck.technologysalesmantoolkit.network

import com.bramdeconinck.technologysalesmantoolkit.interfaces.FirebaseInstructionCallback
import com.bramdeconinck.technologysalesmantoolkit.interfaces.FirebaseServiceCallback
import com.bramdeconinck.technologysalesmantoolkit.interfaces.FirebaseSuggestionCallback
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.createSuggestionData
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.transformSnapshotToSInstruction
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.transformSnapshotToService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Source

class FirestoreAPI {

    // Access a Firebase Firestore instance
    private val firestore = FirebaseFirestore.getInstance()

    init {
        // These settings are used to hide a warning about dates and timestamps
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()
    }

    // Fetch all services from the Firestore
    fun fetchAllServices(firebaseServiceCallback: FirebaseServiceCallback) {
        firebaseServiceCallback.showProgress()
        firestore.collection("Services").get(Source.SERVER)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val servicesList: MutableList<Any> = mutableListOf()
                        for (doc in task.result!!) { servicesList.add(transformSnapshotToService(doc)) }
                        firebaseServiceCallback.onServicesCallBack(servicesList.toList())
                    }
                    else { firebaseServiceCallback.showServicesMessage() }
                    firebaseServiceCallback.hideProgress()
                }
    }

    // Fetch all instructions of a service from the Firestore
    fun fetchAllInstructionsFrom(serviceId: String, firebaseInstructionCallback: FirebaseInstructionCallback) {
        firestore.collection("Instructions").whereEqualTo("serviceId", serviceId).orderBy("index").get(Source.SERVER)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val instructionsList: MutableList<Any> = mutableListOf()
                        for (doc in task.result!!) { instructionsList.add(transformSnapshotToSInstruction(doc)) }
                        firebaseInstructionCallback.onInstructionsCallBack(instructionsList.toList())
                    }
                    else { firebaseInstructionCallback.showInstructionsMessage() }
                }
    }

    // Post a suggestion to the Firestore
    fun postSuggestion(callback: FirebaseSuggestionCallback, suggestion: String) {
        firestore.collection("Suggestions").add(createSuggestionData(suggestion))
                .addOnSuccessListener { callback.showSuccessMessage() }
                .addOnFailureListener { callback.showFailureMessage() }
    }

}