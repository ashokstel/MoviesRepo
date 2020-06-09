package com.restodine.common.network.interceptor

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


private const val TIMESTAMP_HEADER_NAME = "Timestamp"
private const val AUTHENTICATION_HEADER_NAME = "Authentication"
private const val HMAC_SHA256_ALGORITHM = "HmacSHA256"
class AuthInterceptorKt : Interceptor {


    override fun intercept(chain: Interceptor.Chain?): Response? {
        val request = chain?.request()
        return if(request != null) chain.proceed(request) else null
    }

    /*private fun sign(request: Request): Request {
        val key = AppUser.getAccessKey()
        val secret = AppUser.getAccessSecret()
        val bearerToken = AppUser.getBearerToken()

        if (key.isBlank() || secret.isBlank()) {
            return request
        }

        val date = System.currentTimeMillis().toString()
        val message = buildBaseString(request, date)

        val authRequestBuilder = request.newBuilder()

        // Setting Authentication in header
        if (!message.isBlank() && !secret.isBlank()) {
            try {
                //Timber.v("Starting computeHash")
                val authToken = computeHash(secret, message)
                //Timber.v("Ending computeHash")
                authRequestBuilder.addHeader(AUTHENTICATION_HEADER_NAME, "$key:$authToken")
                //Timber.d("$AUTHENTICATION_HEADER_NAME=$key:$authToken")
                authRequestBuilder.addHeader(TIMESTAMP_HEADER_NAME, date)
                //Timber.d("$TIMESTAMP_HEADER_NAME=$date")
            } catch (e: Exception) {
                Timber.e(e)
            }

        }

        // Setting bearer token in header
        if (!bearerToken.isBlank()) {
            try {
                authRequestBuilder.addHeader(KEY_YP_BEARER_TOKEN, bearerToken)
            } catch (e: Exception) {
                Timber.e(e)
            }

        }

        return authRequestBuilder.build()
    }*/

    private fun buildBaseString(request: Request, date: String): String {
        val params = ArrayList<String>()


        //String urlSafe = "";
        val url = request.url().toString().toLowerCase()
        val methodType = request.method()
        val urlPath = getPath(url)
        /*try {
            urlSafe = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException encodingException) {
            urlSafe = url;
        }*/
        val parameterMessage = buildParameterMessage(url)


        params.add(methodType)
        params.add(date)
        params.add(urlPath)
        params.add(parameterMessage)

        return params.joinToString("\n")
    }

    private fun getPath(url: String): String {
        try {
            val uri = URI(url)
            return uri.path
        } catch (e: URISyntaxException) {
            Timber.e(e, "Error while retrieving AUTH path")
        }

        return ""
    }

    private fun getQuery(url: String): String? {
        try {
            val uri = URI(url)
            return uri.query
        } catch (e: URISyntaxException) {
            Timber.e(e, "Error while retrieving AUTH Query")
        }

        return ""
    }

    private fun buildParameterMessage(url: String): String {
        val sortedParams = ArrayList<String>()
        val params = buildParameterCollection(url)
        if (params.isEmpty())
            return ""

        for (key in params.keys) {
            sortedParams.add(key + "=" + params[key])
        }
        return sortedParams.joinToString("&")
    }

    private fun buildParameterCollection(url: String): TreeMap<String, String> {
        val params = TreeMap<String, String>()
        val query = getQuery(url)
        try {
            if (query != null && !query.isBlank()) {
                for (q in query.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                    val p = q.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (p.size == 2) {
                        params[p[0]] = p[1]
                    } else {
                        params[p[0]] = "="
                    }
                }
            }
        }
        catch (e:Exception){
            Timber.e(e, "Error while build auth parameter collection")
        }
        return params
    }

    @Throws(Exception::class)
    private fun computeHash(key: String, data: String): String {
        val sha256HMAC = Mac.getInstance(HMAC_SHA256_ALGORITHM)
        val secretKey = SecretKeySpec(key.toUpperCase().toByteArray(charset("UTF-8")), HMAC_SHA256_ALGORITHM)
        sha256HMAC.init(secretKey)

        val encodedText = sha256HMAC.doFinal(data.toByteArray(charset("UTF-8")))
        return Base64.encodeToString(encodedText, Base64.DEFAULT).trim { it <= ' ' }
    }
}