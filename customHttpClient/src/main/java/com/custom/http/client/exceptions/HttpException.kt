package com.custom.http.client.exceptions

import com.custom.http.client.constant.BLANK_STRING
import com.custom.http.client.response.Response

/**
 * The full HTTP response. This may be null if the exception was serialized.
 */
class HttpException(@Transient val response: Response<*>?): RuntimeException(getMessage(response)) {

    /**
     * HTTP status code.
     */
    val code:Int = response?.code ?: -1

    companion object {
        private fun getMessage(response: Response<*>?): String {
            requireNotNull(response){ "response == null" }
            return "HTTP ${response.code} ${response.message}"
        }
    }

    /**
     * HTTP status message.
     */
    override val message: String = response?.message ?: BLANK_STRING
}