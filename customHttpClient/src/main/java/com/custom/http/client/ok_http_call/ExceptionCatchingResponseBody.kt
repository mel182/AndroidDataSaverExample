package com.custom.http.client.ok_http_call

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

class ExceptionCatchingResponseBody(private val delegate:ResponseBody): ResponseBody() {

    private var delegateSource: BufferedSource = object: ForwardingSource(delegate.source()) {

        override fun read(sink: Buffer, byteCount: Long): Long {

            return try {
                super.read(sink, byteCount)
            } catch (e: java.io.IOException) {
                thrownException = e
                throw e
            }
        }
    }.buffer()

    var thrownException:IOException? = null

    override fun contentLength(): Long = delegate.contentLength()

    override fun contentType(): MediaType? = delegate.contentType()

    override fun source(): BufferedSource = delegateSource

    override fun close() = delegate.close()

    @Throws(java.io.IOException::class)
    fun throwIfCaught() {
        if (thrownException != null) {
            throw thrownException as IOException
        }
    }

}