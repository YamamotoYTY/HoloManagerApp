package com.yatoya.vtubermanager.holder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yatoya.vtubermanager.R

/**
 * RecyclerViewのHolder
 */
class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    /**
     * クリックイベント用のインターフェース
     */
    interface ItemClickListener {
        // クリックイベント押下時
        fun onItemClick(view: View, position: Int)
    }

    val itemChannelNameView: TextView = view.findViewById(R.id.itemChannelNameView)
    val itemLiveImageView: ImageView = view.findViewById(R.id.itemLiveImageView)
    val itemLiveTitleView: TextView = view.findViewById(R.id.itemLiveTitleView)
    val itemLiveBroadcastTypeView: TextView = view.findViewById(R.id.itemLiveBroadcastView)
    val itemLiveCurrentViewersView: TextView = view.findViewById(R.id.itemLiveCurrentViewersView)
    val itemLiveListView: LinearLayout = view.findViewById(R.id.itemLiveListView)

    init {
        // レイアウトの初期設定
    }
}