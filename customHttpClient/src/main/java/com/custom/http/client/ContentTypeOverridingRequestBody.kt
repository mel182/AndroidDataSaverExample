package com.custom.http.client

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink

class ContentTypeOverridingRequestBody(private val delegate:RequestBody, private val contentType: MediaType): RequestBody() {

    override fun contentType(): MediaType = contentType

    override fun contentLength(): Long = delegate.contentLength()

    override fun writeTo(sink: BufferedSink) {
        delegate.writeTo(sink)
    }

}