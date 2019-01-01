package com.bramdeconinck.technologysalesmantoolkit.utils

import com.bramdeconinck.technologysalesmantoolkit.models.Category
import com.bramdeconinck.technologysalesmantoolkit.models.Instruction
import com.bramdeconinck.technologysalesmantoolkit.models.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.QueryDocumentSnapshot

/**
 * [FirebaseUtils] contains all the Firebase utilities.
 */
object FirebaseUtils {

    /**
     * [FirebaseAuth] instance and the current [FirebaseUser].
     */
    @JvmStatic
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    @JvmStatic
    var firebaseUser: FirebaseUser? = firebaseAuth.currentUser

    /**
     * Converting the data of a snapshot to a [Service] object.
     */
    @JvmStatic
    fun transformSnapshotToService(snapshot: QueryDocumentSnapshot): Service {
        return Service(
                id = snapshot.id,
                name = snapshot.getString("name")!!,
                description = snapshot.getString("description")!!,
                category = transformIntToCategory(snapshot.getDouble("category")!!.toInt()),
                created = snapshot.getTimestamp("created")!!,
                price = snapshot.getDouble("price")!!,
                image = snapshot.getString("image")!!
        )
    }

    /**
     * Converting the data of a snapshot to an [Instruction] object.
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
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

    /**
     * Creating suggestion data in a format that can be used to post a Suggestion object to the Firestore.
     */
    @JvmStatic
    fun createSuggestionData(suggestion: String): HashMap<String, Any> {
        val data = HashMap<String, Any>()
        data["message"] = suggestion
        data["sender"] = firebaseUser!!.uid
        return data
    }

    /**
     * Retrieves the right category of a service based on an integer.
     */
    fun transformIntToCategory(i: Int): Category {
        return when (i) {
            0 -> Category.Windows
            1 -> Category.Android
            2 -> Category.Apple
            else -> Category.Andere
        }
    }

    /**
     * Transforms a category enum to an integer for storing in the local database.
     */
    fun transformCategoryToInt(c: Category): Int {
        return when (c) {
            Category.Windows -> 0
            Category.Android -> 1
            Category.Apple -> 2
            Category.Andere -> 3
        }
    }

    /**
     * Create a [UserProfileChangeRequest] object to update the profile of the current [FirebaseUser].
     */
    @JvmStatic
    fun createProfileUpdates(firstname: String, familyname: String): UserProfileChangeRequest {
        return UserProfileChangeRequest
                .Builder()
                .setDisplayName("$firstname $familyname")
                .build()
    }

}