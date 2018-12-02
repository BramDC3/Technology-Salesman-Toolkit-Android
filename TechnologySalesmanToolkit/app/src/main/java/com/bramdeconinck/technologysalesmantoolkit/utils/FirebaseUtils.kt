package com.bramdeconinck.technologysalesmantoolkit.utils

import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QueryDocumentSnapshot

object FirebaseUtils {

    private val mAuth = FirebaseAuth.getInstance()

    @JvmStatic
    // Converting the data of a snapshot to a Service object
    fun fromSnapshotToService(snapshot: QueryDocumentSnapshot): Service {
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

    @JvmStatic
    fun createSuggestionData(suggestion: String): HashMap<String, Any> {
        val data = HashMap<String, Any>()
        data["message"] = suggestion
        data["sender"] = mAuth.currentUser?.uid ?: "Anonymous"
        return data
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

}