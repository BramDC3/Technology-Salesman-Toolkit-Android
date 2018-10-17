package com.bramdeconinck.technologysalesmantoolkit.network

import com.google.firebase.firestore.FirebaseFirestore

public class FirestoreAPI() {

    // Access a Cloud Firestore instance
    val db = FirebaseFirestore.getInstance()

    fun getServicesFromFirebase() {
        db.collection("services")
    }


}