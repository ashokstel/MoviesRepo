package com.restodine.common.network
import androidx.collection.ArrayMap
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.util.regex.Pattern


class ApiResponse<T> {
    val code: Int
    val body: T?
    val error: Throwable?
    private val links: MutableMap<String, String>

    constructor(throwable: Throwable) {
        code = -1
        body = null
        error = throwable
        links = androidx.collection.ArrayMap<String, String>()
    }

    constructor(response: Response<T>) {
        code = response.code()
        if (response.isSuccessful) {
            body = response.body()
            error = null
        } else {
            var message: String? = null
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody()!!.string()
                } catch (ex: IOException) {
                    Timber.e(ex, "error while parsing response")
                }
            }
            if (message == null || message.trim { it <= ' ' }.isEmpty()) {
                message = response.message()
            }
            error = Exception(message)
            body = null
        }
        val linkHeader = response.headers().get("link")
        if (linkHeader == null) {
            links = androidx.collection.ArrayMap<String, String>()
        } else {
            links = androidx.collection.ArrayMap<String, String>()
            val matcher = LINK_PATTERN.matcher(linkHeader)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)] = matcher.group(1)
                }
            }
        }
    }

    val isSuccessful: Boolean
        get() = code in 200..299

    val nextPage: Int?
        get() {
            val next = links[NEXT_LINK] ?: return null
            val matcher = PAGE_PATTERN.matcher(next)
            if (!matcher.find() || matcher.groupCount() != 1) {
                return null
            }
            return try {
                Integer.parseInt(matcher.group(1))
            } catch (ex: NumberFormatException) {
                Timber.e(Exception("cannot parse next page from $next"))
                null
            }

        }

    companion object {
        private val LINK_PATTERN = Pattern
                .compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("page=(\\d)+")
        private const val NEXT_LINK = "next"
    }
}