package com.bramdeconinck.technologysalesmantoolkit.utils

import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
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

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    // Converting the data of a snapshot to an Instruction object
    fun transformSnapshotToSInstruction(snapshot: QueryDocumentSnapshot): Instruction {
        return Instruction(
                id = snapshot.id,
                title = snapshot.getString("title")!!,
                description = snapshot.getString("description")!!,
                content = snapshot.get("content") as List<String>,
                serviceId = snapshot.getString("serviceId")!!,
                image = snapshot.getString("image")!!,
                index =  snapshot.getDouble("index")!!.toInt()
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
            else -> Category.Andere
        }
    }

    @JvmStatic
    fun createProfileUpdates(firstname: String, familyname: String): UserProfileChangeRequest {
        return UserProfileChangeRequest.Builder()
                .setDisplayName("$firstname $familyname")
                .build()
    }

}