package com.edu.sun.oereminder.data.source.remote

import android.accounts.NetworkErrorException
import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.utils.NetConst.CHARSET_UTF8
import com.edu.sun.oereminder.utils.NetConst.CONNECT_TIMEOUT
import com.edu.sun.oereminder.utils.NetConst.READ_TIMEOUT
import com.edu.sun.oereminder.utils.NetConst.RequestMethod
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object HttpUtils {

    fun requestApi(
        @RequestMethod method: String,
        urlString: String,
        headers: Map<String, String>,
        callback: SourceCallback<String>
    ) {
        var httpURLConnection: HttpURLConnection? = null
        try {
            httpURLConnection = (URL(urlString).openConnection() as HttpURLConnection).apply {
                headers.forEach {
                    setRequestProperty(it.key, it.value)
                }
                requestMethod = method
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT
                response(callback)
            }
        } catch (e: MalformedURLException) {
            callback.onError(e)
        } catch (e: IOException) {
            callback.onError(e)
        } finally {
            httpURLConnection?.disconnect()
        }
    }

    fun requestApi(
        @RequestMethod method: String,
        urlString: String,
        headers: Map<String, String>,
        body: Map<String, Any>,
        callback: SourceCallback<String>
    ) {
        val out = StringBuffer().apply {
            body.keys.forEach {
                if (isNotEmpty()) append("&")
                append(it).append("=").append(body[it])
            }
        }
        var httpURLConnection: HttpURLConnection? = null
        try {
            httpURLConnection = (URL(urlString).openConnection() as HttpURLConnection).apply {
                setRequestProperty("accept", "*/*")
                setRequestProperty("connection", "Keep-Alive")
                setRequestProperty("Content-Length", out.length.toString())
                headers.forEach {
                    setRequestProperty(it.key, it.value)
                }

                requestMethod = method
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT
                doInput = true
                doOutput = true

                PrintWriter(outputStream).apply {
                    write(out.toString())
                    flush()
                    close()
                }
                response(callback)
            }
        } catch (e: MalformedURLException) {
            callback.onError(e)
        } catch (e: IOException) {
            callback.onError(e)
        } finally {
            httpURLConnection?.disconnect()
        }
    }

    @Throws(IOException::class)
    private fun HttpURLConnection.response(
        callback: SourceCallback<String>
    ) {
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = inputStream
            val bufferedReader =
                BufferedReader(InputStreamReader(inputStream, CHARSET_UTF8))
            val buffer = StringBuffer()
            var line: String?
            while ((bufferedReader.readLine().also { line = it }) != null) {
                buffer.append(line)
            }
            bufferedReader.close()
            inputStream.close()
            callback.onSuccess(buffer.toString())
        } else {
            callback.onError(NetworkErrorException("Err code: $responseCode"))
        }
    }
}
