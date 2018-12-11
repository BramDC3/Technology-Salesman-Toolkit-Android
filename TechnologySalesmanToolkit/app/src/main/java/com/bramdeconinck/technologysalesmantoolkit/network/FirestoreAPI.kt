package com.bramdeconinck.technologysalesmantoolkit.network

import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseServiceCallback
import com.bramdeconinck.technologysalesmantoolkit.interfaces.IFirebaseSuggestionCallback
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.createSuggestionData
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.transformSnapshotToSInstruction
import com.bramdeconinck.technologysalesmantoolkit.utils.FirebaseUtils.transformSnapshotToService
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
                        val servicesList: MutableList<Any> = mutableListOf()
                        for (doc in task.result!!) { servicesList.add(transformSnapshotToService(doc)) }
                        firebaseServiceCallback.onCallBack(servicesList.toList())
                    }
                    else { firebaseServiceCallback.showMessage() }
                    firebaseServiceCallback.hideProgress()
                }
    }

    // Get all instructions of a service from the Firestore
    fun getAllInstructionsFrom(serviceId: String, firebaseServiceCallback: IFirebaseServiceCallback) {
        firebaseServiceCallback.showProgress()
        firestore.collection("Instructions").whereEqualTo("serviceId", serviceId).orderBy("index").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val instructionsList: MutableList<Any> = mutableListOf()
                        for (doc in task.result!!) { instructionsList.add(transformSnapshotToSInstruction(doc)) }
                        firebaseServiceCallback.onCallBack(instructionsList.toList())
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