@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package si.uni_lj.fri.pbd.miniapp3.rest

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import si.uni_lj.fri.pbd.miniapp3.Constants
import timber.log.Timber
import okhttp3.logging.HttpLoggingInterceptor


object ServiceGenerator {
    private var sBuilder: Retrofit.Builder? = null
    private var sHttpClient: OkHttpClient.Builder? = null
    private var sRetrofit: Retrofit? = null
    private fun init() {
        sHttpClient = OkHttpClient.Builder()
        // Converter
        val gson = GsonBuilder().serializeNulls().serializeNulls().setPrettyPrinting().setVersion(1.0).create()
        sBuilder = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson))


        // Code from https://stackoverflow.com/questions/32965790/retrofit-2-0-how-to-print-the-full-json-response
        // Create interceptor and add it to client
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = sHttpClient?.addInterceptor(interceptor)?.build()
        sRetrofit = sBuilder?.client(client)?.build()
        Timber.d("Retrofit built with base url: %s", sRetrofit?.baseUrl()?.toUrl().toString())
    }

    fun <S> createService(serviceClass: Class<S>?): S {
        return sRetrofit!!.create(serviceClass)
    }

    init {
        init()
    }
}