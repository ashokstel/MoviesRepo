package com.restodine.common.network.interceptor


import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.net.HttpURLConnection


class HttpErrorInterceptor : Interceptor {

    private val KEY_DEPRECATED_HEADER = "X-deprecated-api"
    private val VALUE_DEPRECATED_HEADER = "true"

    override fun intercept(chain: Interceptor.Chain): Response? {

        val response: Response?
        try {
            response = chain.proceed(chain.request())
        } catch (e: Exception) {
            Timber.e(e, "HTTP REQUEST FAILED")
            throw e
        }

        try {
            // To track network failures
            if (response != null && response.code() != HttpURLConnection.HTTP_OK) {
                val statusCode = response.code().toString()
                val requestUrl = response.request().url().url().toString()


            }

            // To track deprecated API calls
            if (response != null && hasDeprecatedHeader(response)) {
                val requestUrl = response.request().url().url().toString()
                Timber.e(Exception("Deprecated API : ".plus(requestUrl)))
            }

            // For any 401 un-authorized response status, perform logout
            if (response?.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                val requestUrl = response.request().url().url().toString()
                Timber.e(Exception("Found 401, Performing logout.."))
            }

        } catch (e: Exception) {
            Timber.e(e, "NetworkResponseInterceptor")
        }

        return response
    }

    private fun hasDeprecatedHeader(response: Response?): Boolean {
        if (response?.headers() != null && response.headers().size() > 0) {
            val value = response.headers().get(KEY_DEPRECATED_HEADER)
            return value?.equals(VALUE_DEPRECATED_HEADER, ignoreCase = true) ?: false
        }
        return false
    }
}