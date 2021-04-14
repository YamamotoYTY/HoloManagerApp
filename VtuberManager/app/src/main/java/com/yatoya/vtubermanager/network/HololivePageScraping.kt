package com.yatoya.vtubermanager.network

import android.net.Uri
import android.util.Log
import com.yatoya.vtubermanager.enum.VtuberName
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import kotlin.concurrent.thread

typealias onVtuberVideoIdResponse = (
    shionVideoIdList: Array<String?>,
    aquaVideoIdList: Array<String?>,
    koroneVideoIdList: Array<String?>,
    marinVideoIdList: Array<String?>,
    lamyVideoIdList: Array<String?>,
    suiseiVideoIdList: Array<String?>,
    pekoraVideoIdList: Array<String?>,
    rushiaVideoIdList: Array<String?>,
    mikoVideoIdList: Array<String?>,
    kanataVideoIdList: Array<String?>
) -> Unit

/**
 * ホロジュールのスクレイピング用クラス
 */
class HololivePageScraping {

    private val TAG = HololivePageScraping::class.java.simpleName

    val HOLOLIVE_PAGE_URL = "https://schedule.hololive.tv/"

    val shionVideoIdList: MutableList<String?> = mutableListOf()
    val aquaVideoIdList: MutableList<String?> = mutableListOf()
    val koroneVideoIdList: MutableList<String?> = mutableListOf()
    val marinVideoIdList: MutableList<String?> = mutableListOf()
    val lamyVideoIdList: MutableList<String?> = mutableListOf()
    val suiseiVideoIdList: MutableList<String?> = mutableListOf()
    val pekoraVideoIdList: MutableList<String?> = mutableListOf()
    val rushiaVideoIdList: MutableList<String?> = mutableListOf()
    val mikoVideoIdList: MutableList<String?> = mutableListOf()
    val kanataVideoIdList: MutableList<String?> = mutableListOf()

    var vtuberVideoIdCallback: onVtuberVideoIdResponse? = null

    /**
     * ページのスクレイピングリクエスト
     */
    fun requestPageScraping() {
        thread {
            // 指定のURLに対してGETでリクエスト送信して取得（post()メソッドならPOSTも可）
            val document: Document?
            val elements: Elements?
            try {
                document = Jsoup.connect(HOLOLIVE_PAGE_URL).get()
                elements = document.select(".thumbnail")
            } catch (e: Exception) {
                vtuberVideoIdCallback?.invoke(
                    emptyArray(),
                    emptyArray(),
                    emptyArray(),
                    emptyArray(),
                    emptyArray(),
                    emptyArray(),
                    emptyArray(),
                    emptyArray(),
                    emptyArray(),
                    emptyArray()
                )
                return@thread
            }

            for (element in elements) {
                val text = element.text()

                if (text.contains(VtuberName.SHION.vtuberName1)) {
                    Log.i(TAG, VtuberName.SHION.vtuberName1)
                    val videoId = getVideoId(element)
                    if (videoId != null) {
                        shionVideoIdList.add(videoId)
                    }
                } else if (text.contains(VtuberName.AQUA.vtuberName1)) {
                    Log.i(TAG, VtuberName.AQUA.vtuberName1)
                    val videoId = getVideoId(element)
                    if (videoId != null) {
                        aquaVideoIdList.add(videoId)
                    }
                } else if (text.contains(VtuberName.KORONE.vtuberName1)) {
                    Log.i(TAG, VtuberName.KORONE.vtuberName1)
                    val videoId = getVideoId(element)
                    if (videoId != null) {
                        koroneVideoIdList.add(videoId)
                    }
                } else if (text.contains(VtuberName.MARIN.vtuberName1)) {
                    Log.i(TAG, VtuberName.MARIN.vtuberName1)
                    val videoId = getVideoId(element)
                    if (videoId != null) {
                        marinVideoIdList.add(videoId)
                    }
                } else if (text.contains(VtuberName.LAMY.vtuberName1)) {
                    Log.i(TAG, VtuberName.LAMY.vtuberName1)
                    val videoId = getVideoId(element)
                    if (videoId != null) {
                        lamyVideoIdList.add(videoId)
                    }
                } else if (text.contains(VtuberName.SUISEI.vtuberName1)) {
                    Log.i(TAG, VtuberName.SUISEI.vtuberName1)
                    val videoId = getVideoId(element)
                    if (videoId != null) {
                        suiseiVideoIdList.add(videoId)
                    }
                } else if (text.contains(VtuberName.PEKORA.vtuberName1)) {
                    Log.i(TAG, VtuberName.PEKORA.vtuberName1)
                    val videoId = getVideoId(element)
                    if (videoId != null) {
                        pekoraVideoIdList.add(videoId)
                    }
                } else if (text.contains(VtuberName.RUSHIA.vtuberName1)) {
                    Log.i(TAG, VtuberName.RUSHIA.vtuberName1)
                    val videoId = getVideoId(element)
                    if (videoId != null) {
                        rushiaVideoIdList.add(videoId)
                    }
                } else if (text.contains(VtuberName.MIKO.vtuberName1)) {
                    Log.i(TAG, VtuberName.MIKO.vtuberName1)
                    val videoId = getVideoId(element)
                    if (videoId != null) {
                        mikoVideoIdList.add(videoId)
                    }
                } else if (text.contains(VtuberName.KANATA.vtuberName1)) {
                    Log.i(TAG, VtuberName.KANATA.vtuberName1)
                    val videoId = getVideoId(element)
                    if (videoId != null) {
                        kanataVideoIdList.add(videoId)
                    }
                }
            }
            vtuberVideoIdCallback?.invoke(
                shionVideoIdList.toTypedArray(),
                aquaVideoIdList.toTypedArray(),
                koroneVideoIdList.toTypedArray(),
                marinVideoIdList.toTypedArray(),
                lamyVideoIdList.toTypedArray(),
                suiseiVideoIdList.toTypedArray(),
                pekoraVideoIdList.toTypedArray(),
                rushiaVideoIdList.toTypedArray(),
                mikoVideoIdList.toTypedArray(),
                kanataVideoIdList.toTypedArray()
            )
        }
    }

    /**
     * ビデオIDを取得
     */
    private fun getVideoId(element: Element): String? {
        val url = element.attr("href")
        Log.i(TAG, "requestPageScraping nodes2:$url")
        val uri: Uri = Uri.parse(url)
        return uri.getQueryParameter("v")
    }
}