package com.bramdeconinck.technologysalesmantoolkit.utils

import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QueryDocumentSnapshot

object FirebaseUtils {

    @JvmStatic
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    @JvmStatic
    var firebaseUser: FirebaseUser? = firebaseAuth.currentUser

    @JvmStatic
    // Converting the data of a snapshot to a Service object
    fun transformSnapshotToService(snapshot: QueryDocumentSnapshot): Service {
        return Service(
                id = snapshot.id,
                name = snapshot.getString("name")!!,
                description = snapshot.getString("description")!!,
                category = transformIntToCategory(snapshot.getDouble("category")!!.toInt()),
                created = snapshot.getTimestamp("created")!!,
                price = snapshot.getDouble("price")!!,
                image = snapshot.getString("image")!!,
                url = snapshot.getString("url")!!
        )
    }

    @JvmStatic
    fun createSuggestionData(suggestion: String): HashMap<String, Any> {
        val data = HashMap<String, Any>()
        data["message"] = suggestion
        data["sender"] = firebaseUser?.uid ?: "Anonymous"
        return data
    }

    // Retrieve the right category of a service based on an integer
    private fun transformIntToCategory(i: Int): Category {
        return when (i) {
            0 -> Category.Windows
            1 -> Category.Android
            2 -> Category.Apple
            else -> Category.Other
        }
    }

}