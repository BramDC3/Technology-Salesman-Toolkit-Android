package com.bramdeconinck.technologysalesmantoolkit.injection

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.bramdeconinck.technologysalesmantoolkit.database.daos.ServiceDao
import com.bramdeconinck.technologysalesmantoolkit.database.ServiceDatabase
import com.bramdeconinck.technologysalesmantoolkit.database.daos.InstructionDao
import com.bramdeconinck.technologysalesmantoolkit.repositories.InstructionRepository
import com.bramdeconinck.technologysalesmantoolkit.repositories.ServiceRepository
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module which provides all required dependencies for the network, database and shared preferences.
 * Methods annotated with @Provides informs Dagger that this method can provide a certain dependency.
 * Methods annotated with @Singleton indicate that Dagger should only instantiate the dependency once and provide that some object on further requests.
 */
@Module
class NetworkModule(private val context: Context) {

    /**
     * Provides the Firestore implementation.
     * The [FirestoreAPI] is used to fetch and post data to the Firestore.
     */
    @Provides
    @Singleton
    internal fun provideFirestoreApi(): FirestoreAPI { return FirestoreAPI() }

    /**
     * Provides the Instruction Repository implementation.
     * The [InstructionRepository] is used to interact with the [InstructionDao].
     */
    @Provides
    @Singleton
    fun provideInstructionRepository(instructionDao: InstructionDao): InstructionRepository { return InstructionRepository(instructionDao) }

    /**
     * Provides the Service Repository implementation.
     * The [ServiceRepository] is used to interact with the [ServiceDao].
     */
    @Provides
    @Singleton
    fun provideServiceRepository(serviceDao: ServiceDao): ServiceRepository { return ServiceRepository(serviceDao) }

    /**
     * Provides the Instruction Dao implementation.
     * The [InstructionDao] is used to interact with the Instruction objects in the [ServiceDatabase].
     */
    @Provides
    @Singleton
    fun provideInstructionDao(serviceDatabase: ServiceDatabase): InstructionDao { return serviceDatabase.instructionDao() }

    /**
     * Provides the Service Dao implementation.
     * The [ServiceDao] is used to interact with the Service objects in the [ServiceDatabase].
     */
    @Provides
    @Singleton
    fun provideServiceDao(serviceDatabase: ServiceDatabase): ServiceDao { return serviceDatabase.serviceDao() }

    /**
     * Provides the Service Database implementation.
     * The [ServiceDatabase] is used to cache Service and Instruction objects.
     */
    @Provides
    @Singleton
    fun provideServiceDatabase(context: Context): ServiceDatabase { return ServiceDatabase.getInstance(context) }

    /**
     * Provides the Shared Preferences implementation.
     * The [SharedPreferences] are used to store the preferred app theme of the user.
     */
    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences { return PreferenceManager.getDefaultSharedPreferences(context) }

    /**
     * Provides the Application Context implementation.
     * The [Context] is used to create the [ServiceDatabase] and provide the [SharedPreferences].
     */
    @Provides
    @Singleton
    fun provideApplicationContext(): Context { return context.applicationContext }

}