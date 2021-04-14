package com.yatoya.vtubermanager.recycler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yatoya.vtubermanager.R
import com.yatoya.vtubermanager.enum.LiveBroadcastType
import com.yatoya.vtubermanager.enum.PriorityValue
import com.yatoya.vtubermanager.holder.HomeViewHolder
import java.util.*
import kotlin.concurrent.schedule

typealias onFinish = () -> Unit

class RecyclerAdapter(
    private val context: Context,
    private val itemClickListener: HomeViewHolder.ItemClickListener,
    private val itemKNameList: List<String?>,
    private val itemLiveTitleList: List<String?>,
    private val itemLiveImageBitmapList: List<Bitmap?>,
    private val itemLiveBroadcastTypeList: List<String?>,
    private val itemLiveStartDateList: List<String?>,
    private val itemLiveCurrentViewersList: List<String?>,
    private val itemLivePriorityList: List<Int?>
) : RecyclerView.Adapter<HomeViewHolder>() {

    private var myRecyclerView: RecyclerView? = null

    var callback: onFinish? = null

    private var timer: Timer? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        myRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        myRecyclerView = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val mView = layoutInflater.inflate(R.layout.home_list_item, parent, false)

        mView.setOnClickListener { view ->
            myRecyclerView?.let {
                itemClickListener.onItemClick(view, it.getChildAdapterPosition(view))
            }
        }

        return HomeViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return itemLiveTitleList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        var liveBroadcastTypeText: String? = null
        var backgroundLiveBroadcastTypeView: Int? = null
        var backgroundLiveBroadcastTypeTextColor: Int? = null
        var currentViewers: String? = null
        when (itemLiveBroadcastTypeList[position]) {
            LiveBroadcastType.LIVE.liveType -> {
                liveBroadcastTypeText = "ライブ配信中"
                backgroundLiveBroadcastTypeView = R.drawable.live_broadcast_of_live_background
                backgroundLiveBroadcastTypeTextColor = Color.RED
                currentViewers = itemLiveCurrentViewersList[position] + "人が視聴中"
            }
            LiveBroadcastType.UPCOMING.liveType -> {
                liveBroadcastTypeText = "ライブ開始"
                if (itemLiveStartDateList.isNotEmpty() && itemLiveStartDateList[position] != null) {
                    liveBroadcastTypeText = itemLiveStartDateList[position] + "\nライブ開始"
                }
                backgroundLiveBroadcastTypeView = R.drawable.live_broadcast_of_upcoming_background
                backgroundLiveBroadcastTypeTextColor = Color.BLUE
            }
        }

        var backgroundLiveListView: Int? = null
        val priorityValue = itemLivePriorityList[position]
        if (priorityValue != null) {
            if (priorityValue >= PriorityValue.HIGH.value) {
                // 優先度「高」の場合
                backgroundLiveListView = R.drawable.live_list_of_high_priority_background
            } else if (priorityValue >= PriorityValue.MIDDLE.value) {
                // 優先度「中」の場合
                backgroundLiveListView = R.drawable.live_list_of_middle_priority_background
            } else if (priorityValue <= PriorityValue.LOW.value) {
                // 優先度「低」の場合
                backgroundLiveListView = R.drawable.live_list_of_low_priority_background
            }
        }

        holder.let {
            it.itemChannelNameView.text = itemKNameList[position]
            it.itemLiveTitleView.text = itemLiveTitleList[position]
            it.itemLiveImageView.setImageBitmap(itemLiveImageBitmapList[position])
            // ライブ情報をいろいろセットする
            // ライブタイプの表示/非表示
            it.itemLiveBroadcastTypeView.visibility =
                if (backgroundLiveBroadcastTypeView != null) TextView.VISIBLE else TextView.GONE

            if (currentViewers != null) {
                // ライブ中の場合、ライブ中の同時接続者を表示する
                it.itemLiveCurrentViewersView.visibility = TextView.VISIBLE
                it.itemLiveCurrentViewersView.text = currentViewers
            } else {
                it.itemLiveCurrentViewersView.visibility = TextView.GONE
            }

            // リストの背景設定
            if (backgroundLiveListView != null) it.itemLiveListView.setBackgroundResource(
                backgroundLiveListView
            )

            // ライブタイプの背景設定
            if (backgroundLiveBroadcastTypeView != null) it.itemLiveBroadcastTypeView.setBackgroundResource(
                backgroundLiveBroadcastTypeView
            )
            // ライブタイプのテキスト設定
            if (backgroundLiveBroadcastTypeView != null) it.itemLiveBroadcastTypeView.text =
                liveBroadcastTypeText
            // ライブタイプのテキストカラー設定
            if (backgroundLiveBroadcastTypeTextColor != null) it.itemLiveBroadcastTypeView.setTextColor(
                backgroundLiveBroadcastTypeTextColor
            )

            // 最後のビュー表示の検知タイマー再起動
            stopDetectionLastTimer()
            startDetectionLastTimer()
        }
    }

    /**
     * 最後のビュー表示の検知タイマー開始
     */
    private fun startDetectionLastTimer() {
        timer = Timer()
        timer?.schedule(500) {
            // 500ms後にタイマーキャンセルされていなければ終了コールバック
            callback?.invoke()
        }
    }

    /**
     * 最後のビュー表示の検知タイマー停止
     */
    private fun stopDetectionLastTimer() {
        timer?.cancel()
        timer = null
    }

}
