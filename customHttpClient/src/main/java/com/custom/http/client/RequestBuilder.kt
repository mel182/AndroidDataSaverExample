package com.custom.http.client

import com.custom.http.client.constant.BLANK_STRING
import com.custom.http.client.constant.DEFAULT_BOOLEAN
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody.Companion.FORM
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import java.util.regex.Pattern

class RequestBuilder(private val method:String,
                     private val baseUrl:HttpUrl? = null,
                     private var relativeUrl:String? = null,
                     private val headers: Headers? = null,
                     private var contentType: MediaType? = null,
                     private val hasBody:Boolean = DEFAULT_BOOLEAN,
                     private val isFormEncoded:Boolean = DEFAULT_BOOLEAN,
                     private val isMultipart:Boolean = DEFAULT_BOOLEAN)
{
    private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
    private val PATH_SEGMENT_ALWAYS_ENCODE_SET = " \"<>^`{}|\\?#"
    private val PATH_TRAVERSAL = Pattern.compile("(.*/)?(\\.|%2e|%2E){1,2}(/.*)?")

    private var requestBuilder: Request.Builder? = null
    private var headersBuilder: Headers.Builder? = null
    private var multipartBuilder: MultipartBody.Builder? = null
    private var formBuilder: FormBody.Builder? = null
    private var urlBuilder: HttpUrl.Builder? = null
    private var body: RequestBody? = null
    //private var httpBaseUrl: HttpUrl? = null

    init {

        headersBuilder = headers?.newBuilder() ?: kotlin.run {
            Headers.Builder()
        }

        if (isFormEncoded) {
            // Will be set to 'body' in 'build'.
            formBuilder = FormBody.Builder()
        } else {
            // Will be set to 'body' in 'build'.
            multipartBuilder = MultipartBody.Builder().apply {
                setType(FORM)
            }
        }
    }

    fun addHeader(name: String?, value: String, allowUnsafeNonAsciiValues: Boolean) {
        if ("Content-Type".equals(name, ignoreCase = true)) {
            contentType = try {
                value.toMediaType()
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Malformed content type: $value $e")
            }
        } else if (allowUnsafeNonAsciiValues) {
            headersBuilder?.addUnsafeNonAscii(name!!, value)
        } else {
            headersBuilder?.add(name!!, value)
        }
    }

    fun addHeaders(headers: Headers) {
        headersBuilder?.addAll(headers)
    }

    fun addPathParam(name: String?, value: String, encoded: Boolean) {
        if (relativeUrl == null) {
            // The relative URL is cleared when the first query parameter is set.
            throw AssertionError()
        }
        val replacement: String = canonicalizeForPath(value, encoded) ?: BLANK_STRING
        val newRelativeUrl = relativeUrl?.replace("{$name}", replacement)
        require(!PATH_TRAVERSAL.matcher(newRelativeUrl).matches())
        { "@Path parameters shouldn't perform path traversal ('.' or '..'): $value" }
        relativeUrl = newRelativeUrl
    }

    private fun canonicalizeForPath(input: String, alreadyEncoded: Boolean): String {
        var codePoint: Int
        var i = 0
        val limit = input.length
        while (i < limit) {
            codePoint = input.codePointAt(i)
            if (codePoint < 0x20 || codePoint >= 0x7f || PATH_SEGMENT_ALWAYS_ENCODE_SET.indexOf(
                    codePoint.toChar()
                ) != -1 || !alreadyEncoded && (codePoint == '/'.code || codePoint == '%'.code)
            ) {
                // Slow path: the character at i requires encoding!
                val out = Buffer()
                out.writeUtf8(input, 0, i)
                canonicalizeForPath(out, input, i, limit, alreadyEncoded)
                return out.readUtf8()
            }
            i += Character.charCount(codePoint)
        }

        // Fast path: no characters required encoding.
        return input
    }

    private fun canonicalizeForPath(out: Buffer, input: String, pos: Int, limit: Int, alreadyEncoded: Boolean) {
        var utf8Buffer: Buffer? = null // Lazily allocated.
        var codePoint: Int
        var i = pos
        while (i < limit) {
            codePoint = input.codePointAt(i)
            if (alreadyEncoded
                && (codePoint == '\t'.code || codePoint == '\n'.code || codePoint == '\u000c'.code || codePoint == '\r'.code)
            ) {
                // Skip this character.
            } else if (codePoint < 0x20 || codePoint >= 0x7f || PATH_SEGMENT_ALWAYS_ENCODE_SET.indexOf(
                    codePoint.toChar()
                ) != -1 || !alreadyEncoded && (codePoint == '/'.code || codePoint == '%'.code)
            ) {
                // Percent encode this character.
                if (utf8Buffer == null) {
                    utf8Buffer = Buffer()
                }
                utf8Buffer.writeUtf8CodePoint(codePoint)
                while (!utf8Buffer.exhausted()) {
                    val b = utf8Buffer.readByte().toInt() and 0xff
                    out.writeByte('%'.code)
                    out.writeByte(HEX_DIGITS[b shr 4 and 0xf].code)
                    out.writeByte(HEX_DIGITS[b and 0xf].code)
                }
            } else {
                // This character doesn't need encoding. Just copy it over.
                out.writeUtf8CodePoint(codePoint)
            }
            i += Character.charCount(codePoint)
        }
    }

    fun addQueryParam(name: String, value: String?, encoded: Boolean) {
        if (relativeUrl != null) {
            // Do a one-time combination of the built relative URL and the base URL.
            require(relativeUrl?.isBlank() ?: true) {
                "relative url is blank"
            }
            urlBuilder = baseUrl?.newBuilder(relativeUrl!!)
            requireNotNull(urlBuilder) { "Malformed URL. Base: $baseUrl, Relative: $relativeUrl" }
            relativeUrl = null
        }
        if (encoded) {
            urlBuilder?.addEncodedQueryParameter(name, value)
        } else {
            urlBuilder?.addQueryParameter(name, value)
        }
    }

    fun setRelativeUrl(relative_url: Any?) {
        relative_url?.let {
            relativeUrl = it.toString()
        }
    }

    fun addFormField(name: String, value: String, encoded: Boolean) {
        if (encoded) {
            formBuilder?.addEncoded(name, value)
        } else {
            formBuilder?.add(name, value)
        }
    }

    fun addPart(headers: Headers, body: RequestBody) {
        multipartBuilder?.addPart(headers, body)
    }

    fun addPart(part: MultipartBody.Part) {
        multipartBuilder?.addPart(part)
    }

    fun setBody(body: RequestBody?) {
        this.body = body
    }

    fun <T> addTag(cls: Class<T>, value: T?) {
        requestBuilder?.tag<T>(cls, value)
    }

    fun get(): Request.Builder? {
        var url: HttpUrl? = null
        this.urlBuilder?.let { url_builder ->
            url = url_builder.build()
        }?: kotlin.run {
            url = baseUrl?.resolve(relativeUrl ?: BLANK_STRING)
            if (url == null)
                throw IllegalArgumentException("Malformed URL. Base: $baseUrl, Relative: $relativeUrl")
        }

        if (this.body == null)
        {
            // Try to pull from one of the builders.
            if (formBuilder != null) {
                this.body = formBuilder?.build()
            } else if (multipartBuilder != null) {
                this.body = multipartBuilder?.build()
            } else if (hasBody) {
                // Body is absent, make an empty body.
                this.body = ByteArray(0).toRequestBody(null, 0, 0)
            }
        }

        if (this.contentType != null) {

            if (this.body != null) {
                this.body = ContentTypeOverridingRequestBody(this.body!!,this.contentType!!)
            } else {
                headersBuilder?.add("Content-Type", contentType.toString())
            }
        }

        return requestBuilder?.url(url!!)?.headers(headersBuilder!!.build())?.method(this.method, body)
    }
}