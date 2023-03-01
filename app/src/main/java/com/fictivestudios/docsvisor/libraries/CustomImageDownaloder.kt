package com.fictivestudios.docsvisor.libraries

import android.content.Context
import com.nostra13.universalimageloader.core.download.BaseImageDownloader
import java.io.IOException
import java.net.HttpURLConnection


class CustomImageDownaloder : BaseImageDownloader {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, connectTimeout: Int, readTimeout: Int) : super(
        context,
        connectTimeout,
        readTimeout
    ) {
    }

    @Throws(IOException::class)
    override fun createConnection(
        url: String,
        extra: Any
    ): HttpURLConnection {
        val conn = super.createConnection(url, extra)
        val headers =
            extra as Map<String, String>
        if (headers != null) {
            for ((key, value) in headers) {
                conn.setRequestProperty(key, value)
            }
        }
        return conn
    }
}