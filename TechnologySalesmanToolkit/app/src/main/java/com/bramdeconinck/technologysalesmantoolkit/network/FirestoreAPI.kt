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

/**
 * The [FirestoreAPI] class is used for all network requests.
 */
class FirestoreAPI {

    /**
     * The [firestore] is an instance of the Firebase Firestore
     */
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * These firestore settings are used to use Timestamp objects instead of Date objects.
     * Date objects are soon going to be deprecated in Firestore.
     */
    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()
    }

    /**
     * Function to fetch all services of the Firestore.
     * [Source.SERVER] is used to disable Firestore caching because a Room database is used for caching.
     *
     * @param firebaseServiceCallback: Interface containing methods that are used as callbacks.
     */
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

    /**
     * Function to fetch all instructions of a gives service of the Firestore.
     * [Source.SERVER] is used to disable Firestore caching because a Room database is used for caching.
     *
     * @param serviceId: Id of the Service for which the instructions need to be fetched.
     * @param firebaseInstructionCallback: Interface containing methods that are used as callbacks.
     */
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

    /**
     * Function to post a suggestion to the Firestore.
     *
     * @param callback: Interface containing methods that are used as callbacks.
     * @param suggestion: String with the suggestion of the user.
     */
    fun postSuggestion(callback: FirebaseSuggestionCallback, suggestion: String) {
        firestore.collection("Suggestions").add(createSuggestionData(suggestion))
                .addOnSuccessListener { callback.showSuccessMessage() }
                .addOnFailureListener { callback.showFailureMessage() }
    }

}