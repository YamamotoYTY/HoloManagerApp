package com.yatoya.vtubermanager

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yatoya.vtubermanager.enum.LiveBroadcastType
import com.yatoya.vtubermanager.holder.HomeViewHolder
import com.yatoya.vtubermanager.live.LiveInfo
import com.yatoya.vtubermanager.network.FileDownloadRequest
import com.yatoya.vtubermanager.network.HololivePageScraping
import com.yatoya.vtubermanager.network.YoutubeApiRequest
import com.yatoya.vtubermanager.recycler.RecyclerAdapter
import com.yatoya.vtubermanager.utils.LivePriorityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivity : Activity(), HomeViewHolder.ItemClickListener {

    private val TAG = HomeActivity::class.java.simpleName

    private lateinit var recyclerView: RecyclerView

    private lateinit var progressView: ContentLoadingProgressBar

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var liveVideoIdListAll: MutableList<String?> = mutableListOf()

    private val livePriorityUtils = LivePriorityUtils()

    private var liveBroadcastTypeListAll: MutableList<String?> = mutableListOf()
    private var liveBroadcastTypeListTemp: MutableList<String?> = mutableListOf()
    private var liveBroadcastTypeListOfLive: MutableList<String?> = mutableListOf()
    private var liveBroadcastTypeListOfUpcoming: MutableList<String?> = mutableListOf()
    private var liveBroadcastTypeListOfNone: MutableList<String?> = mutableListOf()

    private var channelNameListAll: MutableList<String?> = mutableListOf()
    private var channelNameListTemp: MutableList<String?> = mutableListOf()
    private var channelNameListOfLive: MutableList<String?> = mutableListOf()
    private var channelNameListOfUpcoming: MutableList<String?> = mutableListOf()
    private var channelNameListOfNone: MutableList<String?> = mutableListOf()

    private var liveTitleListAll: MutableList<String?> = mutableListOf()
    private var liveTitleListTemp: MutableList<String?> = mutableListOf()
    private var liveTitleListOfLive: MutableList<String?> = mutableListOf()
    private var liveTitleListOfUpcoming: MutableList<String?> = mutableListOf()
    private var liveTitleListOfNone: MutableList<String?> = mutableListOf()

    private var bitmapListAll: MutableList<Bitmap?> = mutableListOf()
    private var bitmapListTemp: MutableList<Bitmap?> = mutableListOf()
    private var bitmapListOfLive: MutableList<Bitmap?> = mutableListOf()
    private var bitmapListOfUpcoming: MutableList<Bitmap?> = mutableListOf()
    private var bitmapListOfNone: MutableList<Bitmap?> = mutableListOf()

    private var liveStartDateListAll: MutableList<String?> = mutableListOf()
    private var liveStartDateListTemp: MutableList<String?> = mutableListOf()
    private var liveStartDateListOfLive: MutableList<String?> = mutableListOf()
    private var liveStartDateListOfUpcoming: MutableList<String?> = mutableListOf()
    private var liveStartDateListOfNone: MutableList<String?> = mutableListOf()

    private var liveCurrentViewersListAll: MutableList<String?> = mutableListOf()
    private var liveCurrentViewersListTemp: MutableList<String?> = mutableListOf()
    private var liveCurrentViewersListOfLive: MutableList<String?> = mutableListOf()
    private var liveCurrentViewersListOfUpcoming: MutableList<String?> = mutableListOf()
    private var liveCurrentViewersListOfNone: MutableList<String?> = mutableListOf()

    private var livePriorityListAll: MutableList<Int?> = mutableListOf()
    private var livePriorityListTemp: MutableList<Int?> = mutableListOf()
    private var livePriorityListOfLive: MutableList<Int?> = mutableListOf()
    private var livePriorityListOfUpcoming: MutableList<Int?> = mutableListOf()
    private var livePriorityListOfNone: MutableList<Int?> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        recyclerView = findViewById(R.id.mainRecyclerView)
        progressView = findViewById(R.id.progressBar)
        swipeRefreshLayout = findViewById(R.id.listSwipeRefreshLayout)

        // ????????????????????????
        livePriorityUtils.initPriorityInfoProcess(this)

        // ????????????????????????
        progressView.visibility = ContentLoadingProgressBar.VISIBLE
        // ???????????????
        updateList()

        swipeRefreshLayout.setOnRefreshListener {
            // ????????????????????????????????????????????????
            init()
            updateList()
        }
    }

    /**
     * ????????????????????????
     */
    private fun updateList() {
        val hololivePageScraping = HololivePageScraping()
        hololivePageScraping.vtuberVideoIdCallback =
            { shionVideoIdList, aquaVideoIdList, koroneVideoIdList,
              marinVideoIdList, lamyVideoIdList, suiseiVideoIdList,
              pekoraVideoIdList, rushiaVideoIdList, mikoVideoIdList, kanataVideoIdList ->
                val videoIdAllList: MutableList<String?> = mutableListOf()

                videoIdAllList.addAll(shionVideoIdList)
                videoIdAllList.addAll(aquaVideoIdList)
                videoIdAllList.addAll(koroneVideoIdList)
                videoIdAllList.addAll(marinVideoIdList)
                videoIdAllList.addAll(lamyVideoIdList)
                videoIdAllList.addAll(suiseiVideoIdList)
                videoIdAllList.addAll(pekoraVideoIdList)
                videoIdAllList.addAll(rushiaVideoIdList)
                videoIdAllList.addAll(mikoVideoIdList)
                videoIdAllList.addAll(kanataVideoIdList)

                val youtubeApiRequest = YoutubeApiRequest()
                youtubeApiRequest.videoInfoCallback =
                    { liveStartDateList, liveCurrentViewersList, liveTitleList
                      , channelNameList, liveImageUrlList, liveBroadcastTypeList, liveVideoIdList ->
                        this.liveVideoIdListAll.addAll(liveVideoIdList)

                        if (liveTitleList.isNotEmpty()) {
                            // ???????????????????????????
                            setRecyclerAdapter(
                                channelNameList,
                                liveTitleList,
                                liveBroadcastTypeList,
                                liveImageUrlList,
                                liveStartDateList,
                                liveCurrentViewersList
                            )
                        }
                    }
                youtubeApiRequest.startRequestVideoSearchApi(videoIdAllList.toTypedArray())
            }
        hololivePageScraping.requestPageScraping()
    }

    /**
     * ????????????????????????????????????
     */
    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(applicationContext, "position $position was tapped", Toast.LENGTH_SHORT)
            .show()
    }

    /**
     * Recycler??????????????????
     */
    private fun setRecyclerAdapter(
        channelNameList: Array<String?>,
        liveTitleList: Array<String?>,
        liveBroadcastTypeList: Array<String?>,
        liveImageUrlList: Array<String?>,
        liveStartDateList: Array<String?>,
        liveCurrentViewersList: Array<String?>
    ) {
        val fileDownloadRequest = FileDownloadRequest()
        // ???????????????????????????????????????????????????
        fileDownloadRequest.callback = { bitmapList ->
            // ???????????????????????????????????????

            val livePriorityList: Array<Int?> = arrayOfNulls(liveTitleList.size)
            // ????????????????????????????????????
            for (i in liveTitleList.indices) {
                val liveTitle = liveTitleList[i]
                val channelName = channelNameList[i]
                if (liveTitle != null && channelName != null) {
                    // ??????????????????????????????????????????????????????????????????
                    livePriorityList[i] =
                        livePriorityUtils.getLivePriority(this, liveTitle, channelName)
                } else {
                    // ???????????????????????????????????????????????????????????????????????????
                    livePriorityList[i] = 1
                }
            }

            // ???????????????????????????
            setSortPriority(
                liveBroadcastTypeList,
                channelNameList,
                liveTitleList,
                bitmapList,
                liveStartDateList,
                liveCurrentViewersList,
                livePriorityList
            )

            // ????????????????????????????????????
            setSortBroadcastType()

            // ??????????????????Bitmap???????????????????????????????????????
            val viewAdapter =
                RecyclerAdapter(
                    this,
                    this,
                    channelNameListAll.toList(),
                    liveTitleListAll.toList(),
                    bitmapListAll.toList(),
                    liveBroadcastTypeListAll.toList(),
                    liveStartDateListAll.toList(),
                    liveCurrentViewersListAll.toList(),
                    livePriorityListAll.toList()
                )

            val layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            viewAdapter.callback = {
                // RecyclerView???????????????????????????
                Log.i(TAG, "viewAdapter callback")
                // ????????????????????????????????????
                goneProgressView()
            }

            // ????????????????????????
            recyclerView.post {
                recyclerView.setHasFixedSize(true)
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = viewAdapter
            }

            if (liveTitleListAll.size == 0) {
                // ????????????0??????????????????????????????????????????????????????????????????????????????????????????99.9%?????????????????????????????????????????????
                goneProgressView()
            }

            // Firebase Realtime Database???????????????????????????
            val firebaseDb: FirebaseDatabase = FirebaseDatabase.getInstance()

            // ?????????Firebase????????????????????????????????????
            for (i in channelNameListAll.indices) {
                val liveInfo = LiveInfo(
                    channelNameListAll[i], liveBroadcastTypeListAll[i],
                    liveTitleListAll[i], liveImageUrlList[i],
                    liveStartDateListAll[i], liveCurrentViewersListAll[i],
                    livePriorityListAll[i]
                )

                // ????????????????????????????????????????????????????????????
                val dbReference: DatabaseReference =
                    firebaseDb.getReference("liveInfo/" + liveVideoIdListAll[i])
                // ?????????????????????????????????
                dbReference.setValue(liveInfo)
            }
        }
        fileDownloadRequest.startFileDownload(liveImageUrlList)
    }

    /**
     * ?????????????????????????????????????????????
     */
    private fun goneProgressView() {
        // ?????????????????????????????????????????????
        GlobalScope.launch(Dispatchers.Main) {
            // ?????????????????????????????????????????????
            progressView.visibility = ContentLoadingProgressBar.GONE

            // ??????????????????????????????????????????????????????
            swipeRefreshLayout.isRefreshing = false
        }
    }

    /**
     * ?????????
     */
    private fun init() {
        liveBroadcastTypeListAll = mutableListOf()
        liveBroadcastTypeListTemp = mutableListOf()
        liveBroadcastTypeListOfLive = mutableListOf()
        liveBroadcastTypeListOfUpcoming = mutableListOf()
        liveBroadcastTypeListOfNone = mutableListOf()

        channelNameListAll = mutableListOf()
        channelNameListTemp = mutableListOf()
        channelNameListOfLive = mutableListOf()
        channelNameListOfUpcoming = mutableListOf()
        channelNameListOfNone = mutableListOf()

        liveTitleListAll = mutableListOf()
        liveTitleListTemp = mutableListOf()
        liveTitleListOfLive = mutableListOf()
        liveTitleListOfUpcoming = mutableListOf()
        liveTitleListOfNone = mutableListOf()

        bitmapListAll = mutableListOf()
        bitmapListTemp = mutableListOf()
        bitmapListOfLive = mutableListOf()
        bitmapListOfUpcoming = mutableListOf()
        bitmapListOfNone = mutableListOf()

        liveStartDateListAll = mutableListOf()
        liveStartDateListTemp = mutableListOf()
        liveStartDateListOfLive = mutableListOf()
        liveStartDateListOfUpcoming = mutableListOf()
        liveStartDateListOfNone = mutableListOf()

        liveCurrentViewersListAll = mutableListOf()
        liveCurrentViewersListTemp = mutableListOf()
        liveCurrentViewersListOfLive = mutableListOf()
        liveCurrentViewersListOfUpcoming = mutableListOf()
        liveCurrentViewersListOfNone = mutableListOf()

        livePriorityListAll = mutableListOf()
        livePriorityListTemp = mutableListOf()
        livePriorityListOfLive = mutableListOf()
        livePriorityListOfUpcoming = mutableListOf()
        livePriorityListOfNone = mutableListOf()
    }

    /**
     * ?????????????????????????????????(??????????????????????????????????????????)
     */
    private fun setSortBroadcastType() {
        Log.i(TAG, "liveBroadcastTypeListTemp")
        for (i in liveBroadcastTypeListTemp.indices) {
            when (val liveBroadcastType = liveBroadcastTypeListTemp[i]) {
                LiveBroadcastType.LIVE.liveType -> {
                    Log.i(TAG, "liveBroadcastTypeListTemp LIVE")
                    liveBroadcastTypeListOfLive.add(liveBroadcastType)
                    channelNameListOfLive.add(channelNameListTemp[i])
                    liveTitleListOfLive.add(liveTitleListTemp[i])
                    bitmapListOfLive.add(bitmapListTemp[i])
                    liveStartDateListOfLive.add(liveStartDateListTemp[i])
                    liveCurrentViewersListOfLive.add(liveCurrentViewersListTemp[i])
                    livePriorityListOfLive.add(livePriorityListTemp[i])
                }
                LiveBroadcastType.UPCOMING.liveType -> {
                    Log.i(TAG, "liveBroadcastTypeListTemp UPCOMING")
                    liveBroadcastTypeListOfUpcoming.add(liveBroadcastType)
                    channelNameListOfUpcoming.add(channelNameListTemp[i])
                    liveTitleListOfUpcoming.add(liveTitleListTemp[i])
                    bitmapListOfUpcoming.add(bitmapListTemp[i])
                    liveStartDateListOfUpcoming.add(liveStartDateListTemp[i])
                    liveCurrentViewersListOfUpcoming.add(liveCurrentViewersListTemp[i])
                    livePriorityListOfUpcoming.add(livePriorityListTemp[i])
                }
                LiveBroadcastType.NONE.liveType -> {
                    Log.i(TAG, "liveBroadcastTypeListTemp NONE")
                    liveBroadcastTypeListOfNone.add(liveBroadcastType)
                    channelNameListOfNone.add(channelNameListTemp[i])
                    liveTitleListOfNone.add(liveTitleListTemp[i])
                    bitmapListOfNone.add(bitmapListTemp[i])
                    liveStartDateListOfNone.add(liveStartDateListTemp[i])
                    liveCurrentViewersListOfNone.add(liveCurrentViewersListTemp[i])
                    livePriorityListOfNone.add(livePriorityListTemp[i])
                }
            }
        }

        channelNameListAll.addAll(channelNameListOfLive)
        channelNameListAll.addAll(channelNameListOfUpcoming)
        channelNameListAll.addAll(channelNameListOfNone)

        liveTitleListAll.addAll(liveTitleListOfLive)
        liveTitleListAll.addAll(liveTitleListOfUpcoming)
        liveTitleListAll.addAll(liveTitleListOfNone)

        bitmapListAll.addAll(bitmapListOfLive)
        bitmapListAll.addAll(bitmapListOfUpcoming)
        bitmapListAll.addAll(bitmapListOfNone)

        liveBroadcastTypeListAll.addAll(liveBroadcastTypeListOfLive)
        liveBroadcastTypeListAll.addAll(liveBroadcastTypeListOfUpcoming)
        liveBroadcastTypeListAll.addAll(liveBroadcastTypeListOfNone)

        liveStartDateListAll.addAll(liveStartDateListOfLive)
        liveStartDateListAll.addAll(liveStartDateListOfUpcoming)
        liveStartDateListAll.addAll(liveStartDateListOfNone)

        liveCurrentViewersListAll.addAll(liveCurrentViewersListOfLive)
        liveCurrentViewersListAll.addAll(liveCurrentViewersListOfUpcoming)
        liveCurrentViewersListAll.addAll(liveCurrentViewersListOfNone)

        livePriorityListAll.addAll(livePriorityListOfLive)
        livePriorityListAll.addAll(livePriorityListOfUpcoming)
        livePriorityListAll.addAll(livePriorityListOfNone)
    }

    /**
     * ???????????????????????????
     */
    private fun setSortPriority(
        liveBroadcastTypeList: Array<String?>,
        channelNameList: Array<String?>,
        liveTitleList: Array<String?>,
        bitmapList: Array<Bitmap?>,
        liveStartDateList: Array<String?>,
        liveCurrentViewersList: Array<String?>,
        livePriorityList: Array<Int?>
    ) {
        val tempTenLivePriorityList: MutableList<Int?> = mutableListOf()
        val tempTenChannelNameList: MutableList<String?> = mutableListOf()
        val tempTenLiveTitleList: MutableList<String?> = mutableListOf()
        val tempTenBitmapList: MutableList<Bitmap?> = mutableListOf()
        val tempTenLiveBroadcastTypeList: MutableList<String?> = mutableListOf()
        val tempTenLiveStartDateList: MutableList<String?> = mutableListOf()
        val tempTenLiveCurrentViewersList: MutableList<String?> = mutableListOf()

        val tempNineLivePriorityList: MutableList<Int?> = mutableListOf()
        val tempNineChannelNameList: MutableList<String?> = mutableListOf()
        val tempNineLiveTitleList: MutableList<String?> = mutableListOf()
        val tempNineBitmapList: MutableList<Bitmap?> = mutableListOf()
        val tempNineLiveBroadcastTypeList: MutableList<String?> = mutableListOf()
        val tempNineLiveStartDateList: MutableList<String?> = mutableListOf()
        val tempNineLiveCurrentViewersList: MutableList<String?> = mutableListOf()

        val tempEightLivePriorityList: MutableList<Int?> = mutableListOf()
        val tempEightChannelNameList: MutableList<String?> = mutableListOf()
        val tempEightLiveTitleList: MutableList<String?> = mutableListOf()
        val tempEightBitmapList: MutableList<Bitmap?> = mutableListOf()
        val tempEightLiveBroadcastTypeList: MutableList<String?> = mutableListOf()
        val tempEightLiveStartDateList: MutableList<String?> = mutableListOf()
        val tempEightLiveCurrentViewersList: MutableList<String?> = mutableListOf()

        val tempSevenLivePriorityList: MutableList<Int?> = mutableListOf()
        val tempSevenChannelNameList: MutableList<String?> = mutableListOf()
        val tempSevenLiveTitleList: MutableList<String?> = mutableListOf()
        val tempSevenBitmapList: MutableList<Bitmap?> = mutableListOf()
        val tempSevenLiveBroadcastTypeList: MutableList<String?> = mutableListOf()
        val tempSevenLiveStartDateList: MutableList<String?> = mutableListOf()
        val tempSevenLiveCurrentViewersList: MutableList<String?> = mutableListOf()

        val tempSixLivePriorityList: MutableList<Int?> = mutableListOf()
        val tempSixChannelNameList: MutableList<String?> = mutableListOf()
        val tempSixLiveTitleList: MutableList<String?> = mutableListOf()
        val tempSixBitmapList: MutableList<Bitmap?> = mutableListOf()
        val tempSixLiveBroadcastTypeList: MutableList<String?> = mutableListOf()
        val tempSixLiveStartDateList: MutableList<String?> = mutableListOf()
        val tempSixLiveCurrentViewersList: MutableList<String?> = mutableListOf()

        val tempFiveLivePriorityList: MutableList<Int?> = mutableListOf()
        val tempFiveChannelNameList: MutableList<String?> = mutableListOf()
        val tempFiveLiveTitleList: MutableList<String?> = mutableListOf()
        val tempFiveBitmapList: MutableList<Bitmap?> = mutableListOf()
        val tempFiveLiveBroadcastTypeList: MutableList<String?> = mutableListOf()
        val tempFiveLiveStartDateList: MutableList<String?> = mutableListOf()
        val tempFiveLiveCurrentViewersList: MutableList<String?> = mutableListOf()

        val tempFourLivePriorityList: MutableList<Int?> = mutableListOf()
        val tempFourChannelNameList: MutableList<String?> = mutableListOf()
        val tempFourLiveTitleList: MutableList<String?> = mutableListOf()
        val tempFourBitmapList: MutableList<Bitmap?> = mutableListOf()
        val tempFourLiveBroadcastTypeList: MutableList<String?> = mutableListOf()
        val tempFourLiveStartDateList: MutableList<String?> = mutableListOf()
        val tempFourLiveCurrentViewersList: MutableList<String?> = mutableListOf()

        val tempThreeLivePriorityList: MutableList<Int?> = mutableListOf()
        val tempThreeChannelNameList: MutableList<String?> = mutableListOf()
        val tempThreeLiveTitleList: MutableList<String?> = mutableListOf()
        val tempThreeBitmapList: MutableList<Bitmap?> = mutableListOf()
        val tempThreeLiveBroadcastTypeList: MutableList<String?> = mutableListOf()
        val tempThreeLiveStartDateList: MutableList<String?> = mutableListOf()
        val tempThreeLiveCurrentViewersList: MutableList<String?> = mutableListOf()

        val tempTwoLivePriorityList: MutableList<Int?> = mutableListOf()
        val tempTwoChannelNameList: MutableList<String?> = mutableListOf()
        val tempTwoLiveTitleList: MutableList<String?> = mutableListOf()
        val tempTwoBitmapList: MutableList<Bitmap?> = mutableListOf()
        val tempTwoLiveBroadcastTypeList: MutableList<String?> = mutableListOf()
        val tempTwoLiveStartDateList: MutableList<String?> = mutableListOf()
        val tempTwoLiveCurrentViewersList: MutableList<String?> = mutableListOf()

        val tempOneLivePriorityList: MutableList<Int?> = mutableListOf()
        val tempOneChannelNameList: MutableList<String?> = mutableListOf()
        val tempOneLiveTitleList: MutableList<String?> = mutableListOf()
        val tempOneBitmapList: MutableList<Bitmap?> = mutableListOf()
        val tempOneLiveBroadcastTypeList: MutableList<String?> = mutableListOf()
        val tempOneLiveStartDateList: MutableList<String?> = mutableListOf()
        val tempOneLiveCurrentViewersList: MutableList<String?> = mutableListOf()

        for (i in livePriorityList.indices) {
            when (val livePriority = livePriorityList[i]) {
                10 -> {
                    tempTenLivePriorityList.add(livePriority)
                    tempTenChannelNameList.add(channelNameList[i])
                    tempTenLiveTitleList.add(liveTitleList[i])
                    tempTenBitmapList.add(bitmapList[i])
                    tempTenLiveBroadcastTypeList.add(liveBroadcastTypeList[i])
                    tempTenLiveStartDateList.add(liveStartDateList[i])
                    tempTenLiveCurrentViewersList.add(liveCurrentViewersList[i])
                }
                9 -> {
                    tempNineLivePriorityList.add(livePriority)
                    tempNineChannelNameList.add(channelNameList[i])
                    tempNineLiveTitleList.add(liveTitleList[i])
                    tempNineBitmapList.add(bitmapList[i])
                    tempNineLiveBroadcastTypeList.add(liveBroadcastTypeList[i])
                    tempNineLiveStartDateList.add(liveStartDateList[i])
                    tempNineLiveCurrentViewersList.add(liveCurrentViewersList[i])
                }
                8 -> {
                    tempEightLivePriorityList.add(livePriority)
                    tempEightChannelNameList.add(channelNameList[i])
                    tempEightLiveTitleList.add(liveTitleList[i])
                    tempEightBitmapList.add(bitmapList[i])
                    tempEightLiveBroadcastTypeList.add(liveBroadcastTypeList[i])
                    tempEightLiveStartDateList.add(liveStartDateList[i])
                    tempEightLiveCurrentViewersList.add(liveCurrentViewersList[i])
                }
                7 -> {
                    tempSevenLivePriorityList.add(livePriority)
                    tempSevenChannelNameList.add(channelNameList[i])
                    tempSevenLiveTitleList.add(liveTitleList[i])
                    tempSevenBitmapList.add(bitmapList[i])
                    tempSevenLiveBroadcastTypeList.add(liveBroadcastTypeList[i])
                    tempSevenLiveStartDateList.add(liveStartDateList[i])
                    tempSevenLiveCurrentViewersList.add(liveCurrentViewersList[i])
                }
                6 -> {
                    tempSixLivePriorityList.add(livePriority)
                    tempSixChannelNameList.add(channelNameList[i])
                    tempSixLiveTitleList.add(liveTitleList[i])
                    tempSixBitmapList.add(bitmapList[i])
                    tempSixLiveBroadcastTypeList.add(liveBroadcastTypeList[i])
                    tempSixLiveStartDateList.add(liveStartDateList[i])
                    tempSixLiveCurrentViewersList.add(liveCurrentViewersList[i])
                }
                5 -> {
                    tempFiveLivePriorityList.add(livePriority)
                    tempFiveChannelNameList.add(channelNameList[i])
                    tempFiveLiveTitleList.add(liveTitleList[i])
                    tempFiveBitmapList.add(bitmapList[i])
                    tempFiveLiveBroadcastTypeList.add(liveBroadcastTypeList[i])
                    tempFiveLiveStartDateList.add(liveStartDateList[i])
                    tempFiveLiveCurrentViewersList.add(liveCurrentViewersList[i])
                }
                4 -> {
                    tempFourLivePriorityList.add(livePriority)
                    tempFourChannelNameList.add(channelNameList[i])
                    tempFourLiveTitleList.add(liveTitleList[i])
                    tempFourBitmapList.add(bitmapList[i])
                    tempFourLiveBroadcastTypeList.add(liveBroadcastTypeList[i])
                    tempFourLiveStartDateList.add(liveStartDateList[i])
                    tempFourLiveCurrentViewersList.add(liveCurrentViewersList[i])
                }
                3 -> {
                    tempThreeLivePriorityList.add(livePriority)
                    tempThreeChannelNameList.add(channelNameList[i])
                    tempThreeLiveTitleList.add(liveTitleList[i])
                    tempThreeBitmapList.add(bitmapList[i])
                    tempThreeLiveBroadcastTypeList.add(liveBroadcastTypeList[i])
                    tempThreeLiveStartDateList.add(liveStartDateList[i])
                    tempThreeLiveCurrentViewersList.add(liveCurrentViewersList[i])
                }
                2 -> {
                    tempTwoLivePriorityList.add(livePriority)
                    tempTwoChannelNameList.add(channelNameList[i])
                    tempTwoLiveTitleList.add(liveTitleList[i])
                    tempTwoBitmapList.add(bitmapList[i])
                    tempTwoLiveBroadcastTypeList.add(liveBroadcastTypeList[i])
                    tempTwoLiveStartDateList.add(liveStartDateListAll[i])
                    tempTwoLiveCurrentViewersList.add(liveCurrentViewersListAll[i])
                }
                1 -> {
                    tempOneLivePriorityList.add(livePriority)
                    tempOneChannelNameList.add(channelNameList[i])
                    tempOneLiveTitleList.add(liveTitleList[i])
                    tempOneBitmapList.add(bitmapList[i])
                    tempOneLiveBroadcastTypeList.add(liveBroadcastTypeList[i])
                    tempOneLiveStartDateList.add(liveStartDateListAll[i])
                    tempOneLiveCurrentViewersList.add(liveCurrentViewersListAll[i])
                }
            }
        }

        liveBroadcastTypeListTemp.addAll(tempTenLiveBroadcastTypeList)
        liveBroadcastTypeListTemp.addAll(tempNineLiveBroadcastTypeList)
        liveBroadcastTypeListTemp.addAll(tempEightLiveBroadcastTypeList)
        liveBroadcastTypeListTemp.addAll(tempSevenLiveBroadcastTypeList)
        liveBroadcastTypeListTemp.addAll(tempSixLiveBroadcastTypeList)
        liveBroadcastTypeListTemp.addAll(tempFiveLiveBroadcastTypeList)
        liveBroadcastTypeListTemp.addAll(tempFourLiveBroadcastTypeList)
        liveBroadcastTypeListTemp.addAll(tempThreeLiveBroadcastTypeList)
        liveBroadcastTypeListTemp.addAll(tempTwoLiveBroadcastTypeList)
        liveBroadcastTypeListTemp.addAll(tempOneLiveBroadcastTypeList)

        channelNameListTemp.addAll(tempTenChannelNameList)
        channelNameListTemp.addAll(tempNineChannelNameList)
        channelNameListTemp.addAll(tempEightChannelNameList)
        channelNameListTemp.addAll(tempSevenChannelNameList)
        channelNameListTemp.addAll(tempSixChannelNameList)
        channelNameListTemp.addAll(tempFiveChannelNameList)
        channelNameListTemp.addAll(tempFourChannelNameList)
        channelNameListTemp.addAll(tempThreeChannelNameList)
        channelNameListTemp.addAll(tempTwoChannelNameList)
        channelNameListTemp.addAll(tempOneChannelNameList)

        liveTitleListTemp.addAll(tempTenLiveTitleList)
        liveTitleListTemp.addAll(tempNineLiveTitleList)
        liveTitleListTemp.addAll(tempEightLiveTitleList)
        liveTitleListTemp.addAll(tempSevenLiveTitleList)
        liveTitleListTemp.addAll(tempSixLiveTitleList)
        liveTitleListTemp.addAll(tempFiveLiveTitleList)
        liveTitleListTemp.addAll(tempFourLiveTitleList)
        liveTitleListTemp.addAll(tempThreeLiveTitleList)
        liveTitleListTemp.addAll(tempTwoLiveTitleList)
        liveTitleListTemp.addAll(tempOneLiveTitleList)

        bitmapListTemp.addAll(tempTenBitmapList)
        bitmapListTemp.addAll(tempNineBitmapList)
        bitmapListTemp.addAll(tempEightBitmapList)
        bitmapListTemp.addAll(tempSevenBitmapList)
        bitmapListTemp.addAll(tempSixBitmapList)
        bitmapListTemp.addAll(tempFiveBitmapList)
        bitmapListTemp.addAll(tempFourBitmapList)
        bitmapListTemp.addAll(tempThreeBitmapList)
        bitmapListTemp.addAll(tempTwoBitmapList)
        bitmapListTemp.addAll(tempOneBitmapList)

        liveStartDateListTemp.addAll(tempTenLiveStartDateList)
        liveStartDateListTemp.addAll(tempNineLiveStartDateList)
        liveStartDateListTemp.addAll(tempEightLiveStartDateList)
        liveStartDateListTemp.addAll(tempSevenLiveStartDateList)
        liveStartDateListTemp.addAll(tempSixLiveStartDateList)
        liveStartDateListTemp.addAll(tempFiveLiveStartDateList)
        liveStartDateListTemp.addAll(tempFourLiveStartDateList)
        liveStartDateListTemp.addAll(tempThreeLiveStartDateList)
        liveStartDateListTemp.addAll(tempTwoLiveStartDateList)
        liveStartDateListTemp.addAll(tempOneLiveStartDateList)

        liveCurrentViewersListTemp.addAll(tempTenLiveCurrentViewersList)
        liveCurrentViewersListTemp.addAll(tempNineLiveCurrentViewersList)
        liveCurrentViewersListTemp.addAll(tempEightLiveCurrentViewersList)
        liveCurrentViewersListTemp.addAll(tempSevenLiveCurrentViewersList)
        liveCurrentViewersListTemp.addAll(tempSixLiveCurrentViewersList)
        liveCurrentViewersListTemp.addAll(tempFiveLiveCurrentViewersList)
        liveCurrentViewersListTemp.addAll(tempFourLiveCurrentViewersList)
        liveCurrentViewersListTemp.addAll(tempThreeLiveCurrentViewersList)
        liveCurrentViewersListTemp.addAll(tempTwoLiveCurrentViewersList)
        liveCurrentViewersListTemp.addAll(tempOneLiveCurrentViewersList)

        livePriorityListTemp.addAll(tempTenLivePriorityList)
        livePriorityListTemp.addAll(tempNineLivePriorityList)
        livePriorityListTemp.addAll(tempEightLivePriorityList)
        livePriorityListTemp.addAll(tempSevenLivePriorityList)
        livePriorityListTemp.addAll(tempSixLivePriorityList)
        livePriorityListTemp.addAll(tempFiveLivePriorityList)
        livePriorityListTemp.addAll(tempFourLivePriorityList)
        livePriorityListTemp.addAll(tempThreeLivePriorityList)
        livePriorityListTemp.addAll(tempTwoLivePriorityList)
        livePriorityListTemp.addAll(tempOneLivePriorityList)
    }
}