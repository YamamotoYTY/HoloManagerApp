package com.yatoya.vtubermanager.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.concurrent.thread

typealias onBitmapResponse = (bitmap: Array<Bitmap?>) -> Unit

/**
 * ファイルダウンロードリクエスト用クラス
 */
class FileDownloadRequest : okhttp3.Callback {

    private val TAG = FileDownloadRequest::class.java.simpleName

    var callback: onBitmapResponse? = null

    private val okHttpClient = OkHttpClient()

    private var urlNumber: Int = 0

    private lateinit var urlList: Array<String?>

    private lateinit var bitmapList: Array<Bitmap?>

    /**
     * ファイルダウンロード開始
     *
     * @param urls URLのリスト
     */
    fun startFileDownload(urls: Array<String?>) {
        Log.i(TAG, "startFileDownload.")
        urlList = urls
        bitmapList = arrayOfNulls(urls.size)
        downloadFile()
    }

    /**
     * ファイルダウンロード
     */
    private fun downloadFile() {
        if (urlList.size == urlNumber) {
            // ファイルダウンロードする物がもうない場合、終了する
            callback?.invoke(bitmapList)
            return
        }
        // ダウンロード用のURLを取得
        val url: String? = urlList[urlNumber]

        if (url == null) {
            // URLがない場合、終了する
            callback?.invoke(bitmapList)
            return
        }
        // ダウンロード用のリクエストを作成
        val request = Request.Builder().url(url).get().build()
        // 通信はバックグラウンドスレッドで行う
        thread {
            // ダウンロードリクエストを叩く
            okHttpClient.newCall(request).enqueue(this)
        }
    }

    /**
     * @see okhttp3.Callback.onResponse()
     */
    override fun onResponse(call: Call, response: Response) {
        Log.i(TAG, "responseCode:" + response.code)
        // レスポンスからファイルをBitmap形式にデコードする
        val bitmap: Bitmap = BitmapFactory.decodeStream(response.body!!.byteStream())

        // Bitmapをリストに詰める
        bitmapList[urlNumber] = bitmap

        urlNumber++
        // 再度ファイルダウンロードを行う
        downloadFile()
    }

    /**
     * @see okhttp3.Callback.onFailure()
     */
    override fun onFailure(call: Call, e: IOException) {
        Log.e(TAG, "" + e)
        callback?.invoke(emptyArray())
    }
}