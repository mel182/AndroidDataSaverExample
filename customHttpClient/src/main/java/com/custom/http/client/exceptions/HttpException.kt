package com.custom.http.client.exceptions

import com.custom.http.client.Response

class HttpException(@Transient val response: Response<*>?): RuntimeException(getMessage(response)) {

    val code:Int = response?.code ?: -1

    companion object {
        private fun getMessage(response: Response<*>?): String {
            requireNotNull(response){ "response == null" }
            return "HTTP ${response.code} ${response.message}"
        }
    }

}