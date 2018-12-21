package com.bramdeconinck.technologysalesmantoolkit.injection.module

import android.content.Context
import com.bramdeconinck.technologysalesmantoolkit.database.ServiceDao
import com.bramdeconinck.technologysalesmantoolkit.database.ServiceDatabase
import com.bramdeconinck.technologysalesmantoolkit.models.ServiceRepository
import com.bramdeconinck.technologysalesmantoolkit.network.FirestoreAPI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Module which provides all required dependencies for the network.
 *
 * Object: Singleton Instance see [The Kotlin reference](https://kotlinlang.org/docs/reference/object-declarations.html)
 * Retrofit: Library used for REST connections. See [The Retrofit reference](https://square.github.io/retrofit/)
 * What is Dependency Injection? See this [video](https://www.youtube.com/watch?v=IKD2-MAkXyQ)
 *
 * Methods annotated with @Provides informs Dagger that this method can provide a certain dependency.
 * Methods annotated with @Singleton indicate that Dagger should only instantiate the dependency
 *  once and provide that some object on further requests.
 */
@Module
class NetworkModule(private val context: Context) {

    /**
     * Provides the Firestore implemenation
     */
    @Provides
    @Singleton
    internal fun provideFirestoreApi(): FirestoreAPI {
        return FirestoreAPI()
    }

    @Provides
    @Singleton
    fun provideServiceRepository(serviceDao: ServiceDao): ServiceRepository {
        return ServiceRepository(serviceDao)
    }

    @Provides
    @Singleton
    fun provideServiceDao(serviceDatabase: ServiceDatabase): ServiceDao {
        return serviceDatabase.serviceDao()
    }

    @Provides
    @Singleton
    fun provideServiceDatabase(context: Context): ServiceDatabase {
        return ServiceDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return context.applicationContext
    }

}