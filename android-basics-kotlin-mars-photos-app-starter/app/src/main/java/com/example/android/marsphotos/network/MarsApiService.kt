package com.example.android.marsphotos.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"


/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 * Moshi parses JSON and converts it to Kotlin objects
 * It needs a data class that describes the json structure (MarsPhoto)
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


/**
 * The Retrofit object with the Moshi converter.
 * Retrofit allows to connect to a REST web service on the internet and get a response.
 */
// Retrofit needs a URI for the web service and a ConverterFactory to build a web services API.
// The converter tells what to do with the data it gets back from the service.
// Retrofit should fetch a JSON response from the web service, and return it as an object.
private val retrofit = Retrofit.Builder()
    // Use Moshi converter factory with the moshi object that was created earlier
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getPhotos] method
 */
interface MarsApiService {
    /**
     * Returns a [List] of [MarsPhoto] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "photos" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("photos")
    // Make function a suspend so that it can be called from within a coroutine
    // Return a list of MarsPhotos using Retrofit with Moshi converter factory
    // Suspend signals that a block of code or function can be paused or resumed
    suspend fun getPhotos(): List<MarsPhoto>

}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
// Creating& initializing an Api object is expensive, so exposing it as an object here allows to save resources
// and to have a single object initialized lazily exposed tp the whole app
object MarsApi {
    // "lazy instantiation" is when object creation is purposely delayed until you actually need that
    // object to avoid unnecessary computation or use of other computing resources.
    val retrofitService : MarsApiService by lazy {
        // Initialize the retrofitService variable using the retrofit.create() method with the MarsApiService interface.
        retrofit.create(MarsApiService::class.java)
    }
}