package com.yatoya.vtubermanager.network

import android.util.Log
import com.yatoya.vtubermanager.enum.LiveBroadcastType
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

typealias onVideoInfoResponse = (
    liveStartDateList: Array<String?>,
    liveCurrentViewersList: Array<String?>,
    liveTitleList: Array<String?>,
    channelNameList: Array<String?>,
    liveImageUrlList: Array<String?>,
    liveBroadcastTypeList: Array<String?>,
    liveVideoIdList: Array<String?>
) -> Unit

/**
 * Youtube APIのリクエスト用クラス
 */
class YoutubeApiRequest {

    private val TAG = YoutubeApiRequest::class.java.simpleName

    private val VIDEO_SEARCH_API_URL = "https://www.googleapis.com/youtube/v3/videos?"

    // TODO:: YoutubeのAPIキーを入れる
    private val API_KEY = ""

    private var liveTitleList: MutableList<String?> = mutableListOf()
    private var channelNameList: MutableList<String?> = mutableListOf()
    private var liveImageUrlList: MutableList<String?> = mutableListOf()
    private var liveBroadcastTypeList: MutableList<String?> = mutableListOf()

    private var liveStartDateList: MutableList<String?> = mutableListOf()
    private var liveCurrentViewersList: MutableList<String?> = mutableListOf()

    private var liveVideoIdList: MutableList<String?> = mutableListOf()

    private lateinit var videoIdList: Array<String?>

    var videoInfoCallback: onVideoInfoResponse? = null

    var starNumber: Int = 0

    /**
     * Youtube Video APIの接続開始
     */
    fun startRequestVideoSearchApi(videoIds: Array<String?>) {
        Log.i(TAG, "startFileDownload.")
        videoIdList = videoIds
        requestVideoSearchApi()
    }

    /**
     * Youtube Video APIの接続
     * @see https://developers.google.com/youtube/v3/docs/videos/list?hl=ja
     */
    private fun requestVideoSearchApi() {
        if (videoIdList.size == starNumber) {
            videoInfoCallback?.invoke(
                liveStartDateList.toTypedArray(),
                liveCurrentViewersList.toTypedArray(),
                liveTitleList.toTypedArray(),
                channelNameList.toTypedArray(),
                liveImageUrlList.toTypedArray(),
                liveBroadcastTypeList.toTypedArray(),
                liveVideoIdList.toTypedArray()
            )
            return
        }
        // ビデオIDを取得
        val videoId: String? = videoIdList[starNumber]

        // APIのパラメータを取得
        val param = "part=snippet,liveStreamingDetails" +
                "&key=" + API_KEY +
                "&id=" + videoId

        // video apiを叩く
        requestApi(VIDEO_SEARCH_API_URL, param, object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.i(TAG, "requestVideoSearchApi responseCode:" + response.code)
                var responseBody: String? = null
                try {
                    responseBody = response.body?.string().toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (responseBody == null) {
                    videoFailureProcess()
                    return
                }
                // Jsonデータを取得
                val jsonData = JSONObject(responseBody)

                // ライブ公開開始時間をセットする
                this@YoutubeApiRequest.setLiveInfoFromJson(jsonData)

                // 再度Youtube Video APIの接続を行う
                restartRequestVideoSearchApi()
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "" + e)
                videoFailureProcess()
            }
        })
    }

    /**
     * 再度Youtube Video APIの接続を行う
     */
    private fun restartRequestVideoSearchApi() {
        starNumber++
        requestVideoSearchApi()
    }

    /**
     * APIを叩く
     * @param url URL
     * @param param パラメータ
     * @param callback Callback
     */
    private fun requestApi(url: String, param: String, callback: Callback) {
        val okHttpClient = OkHttpClient()
        // APIリクエストを取得する
        val request = Request.Builder().url(url + param).get().build()
        // 通信はバックグラウンドスレッドで行う
        thread {
            // APIを叩く
            okHttpClient.newCall(request).enqueue(callback)
        }
    }

    /**
     * ライブ開始時間をセット
     * @param responseJsonData jsonデータ
     */
    private fun setLiveInfoFromJson(responseJsonData: JSONObject) {
        try {
            val itemsArray = responseJsonData.getJSONArray("items")
            for (i in 0 until itemsArray.length()) {
                val items = itemsArray.getJSONObject(i)
                var liveStreamingDetails: JSONObject?
                try {
                    liveStreamingDetails = items.getJSONObject("liveStreamingDetails")
                } catch (e: Exception) {
                    // データが全くないのでおそらくネットワークエラー
                    videoFailureProcess()
                    return
                }

                var isLive = false
                try {
                    // 同時接続者数を取得する
                    liveCurrentViewersList.add(liveStreamingDetails.getString("concurrentViewers"))
                    val activeLiveChatId = liveStreamingDetails.getString("activeLiveChatId")
                    Log.i(TAG, "activeLiveChatId:$activeLiveChatId")
                    isLive = true
                } catch (e: Exception) {
                    // ライブ中ではない場合はここに来る
                }
                if (!isLive) {
                    // ライブ配信中ではない場合
                    try {
                        // ライブが終わっていないかをライブ終了時間があるかで確認する
                        liveStreamingDetails.getString("actualEndTime")
                        // TODO:: ライブアーカイブを表示する場合は以下のコメントアウトを活性化させる
                        setLiveInfo(items)
                        liveBroadcastTypeList.add(LiveBroadcastType.NONE.liveType)
                        liveStartDateList.add("")
                        liveCurrentViewersList.add("")
                    } catch (e: Exception) {
                        // ライブ公開前の場合はここに来る
                        // ライブ公開開始時間を取得する
                        val liveStartDate = liveStreamingDetails.getString("scheduledStartTime")
                        liveStartDateList.add(getDateFormatter(liveStartDate))
                        // ライブ動画情報を取得する
                        setLiveInfo(items)
                        liveBroadcastTypeList.add(LiveBroadcastType.UPCOMING.liveType)
                        // ライブ公開前だが今後の処理(View表示)のためにダミーの値を入れておく
                        liveCurrentViewersList.add("")
                    }
                } else {
                    // ライブ中の場合、ライブ動画情報を取得する
                    setLiveInfo(items)
                    liveBroadcastTypeList.add(LiveBroadcastType.LIVE.liveType)
                    // ライブ中だが今後の処理(View表示)のためにダミーの値を入れておく
                    liveStartDateList.add("")
                }
                liveVideoIdList.add(videoIdList[starNumber])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * フォーマットした日時を取得
     */
    private fun getDateFormatter(utcDateString: String?): String? {
        if (utcDateString == null) {
            return null
        }
        var ds = utcDateString
        ds = ds.replace("-", "/")
        ds = ds.replace("T", " ")
        ds = ds.replace("Z", "")

        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN)

        var dateTmp: Date? = null

        try {
            dateTmp = sdf.parse(ds)
        } catch (e: ParseException) {
            Log.e(TAG, "" + e)
        }
        if (dateTmp == null) return null

        val calendar = Calendar.getInstance()
        calendar.time = dateTmp
        // Youtubeで取得した日時がUTC形式なので9時間追加する
        calendar.add(Calendar.HOUR_OF_DAY, 9)
        val convertDate = calendar.time

        return sdf.format(convertDate)
    }

    /**
     * ライブ情報をセットする
     */
    private fun setLiveInfo(items: JSONObject) {
        val snippet = items.getJSONObject("snippet")
        // ライブタイトルを取得する
        liveTitleList.add(snippet.getString("title"))

        // チャンネル名を取得する
        channelNameList.add(snippet.getString("channelTitle"))

        // ライブのサムネイルを取得する
        val thumbnails = snippet.getJSONObject("thumbnails")
        val mediumImageUrl = thumbnails.getJSONObject("high")
        liveImageUrlList.add(mediumImageUrl.getString("url"))
    }

    /**
     * Youtube Video APIの接続に失敗した時のコールバック処理
     */
    private fun videoFailureProcess() {
        videoInfoCallback?.invoke(
            emptyArray(), emptyArray(), emptyArray(), emptyArray(), emptyArray(),
            emptyArray(), emptyArray()
        )
    }
}