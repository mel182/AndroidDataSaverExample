package com.custom.http.client.ok_http_call

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.BufferedSource

class NoContentResponseBody(private val contentType:MediaType?, private val contentLength:Long = 0L): ResponseBody() {

    override fun contentLength(): Long = contentLength

    override fun contentType(): MediaType? = contentType

    override fun source(): BufferedSource {
        throw IllegalStateException("Cannot read raw response body of a converted body.")
    }

}