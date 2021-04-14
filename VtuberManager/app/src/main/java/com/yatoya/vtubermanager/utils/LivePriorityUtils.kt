package com.yatoya.vtubermanager.utils

import android.content.Context
import com.yatoya.vtubermanager.enum.PriorityKey
import com.yatoya.vtubermanager.enum.VtuberName

/**
 * ライブ優先度ユーティリティ
 */
class LivePriorityUtils {

    /**
     * 優先度情報の初期設定
     * @param context Context
     */
    fun initPriorityInfoProcess(context: Context) {
        PreferencesUtils.savePriorityInfo(context, PriorityKey.SHION.priorityKey, 5)
        PreferencesUtils.savePriorityInfo(context, PriorityKey.AQUA.priorityKey, 4)
        PreferencesUtils.savePriorityInfo(context, PriorityKey.KORONE.priorityKey, 5)
        PreferencesUtils.savePriorityInfo(context, PriorityKey.MARIN.priorityKey, 5)
        PreferencesUtils.savePriorityInfo(context, PriorityKey.LAMY.priorityKey, 4)
        PreferencesUtils.savePriorityInfo(context, PriorityKey.SUISEI.priorityKey, 4)
        PreferencesUtils.savePriorityInfo(context, PriorityKey.PEKORA.priorityKey, 3)
        PreferencesUtils.savePriorityInfo(context, PriorityKey.RUSHIA.priorityKey, 3)
        PreferencesUtils.savePriorityInfo(context, PriorityKey.MIKO.priorityKey, 2)
        PreferencesUtils.savePriorityInfo(context, PriorityKey.KANATA.priorityKey, 2)
    }

    /**
     * ライブ優先度情報を取得する
     *
     * @param context Context
     * @param liveTitle ライブタイトル
     * @param vtuberName Vtuber名
     * @return ライブ優先度情報(1～10)
     */
    fun getLivePriority(context: Context, liveTitle: String, vtuberName: String): Int {
        // ライブタイトルの優先度を取得する
        val checkLiveTitlePriorityUtils = CheckLiveTitlePriorityUtils()
        val liveTitlePriority = checkLiveTitlePriorityUtils.checkLiveTitlePriority(liveTitle)

        // Vtuberの優先度を取得する
        var preferencesKey: String? = null

        if (vtuberName.contains(VtuberName.KORONE.vtuberName2)) {
            preferencesKey = PriorityKey.KORONE.priorityKey
        } else if (vtuberName.contains(VtuberName.SHION.vtuberName2)) {
            preferencesKey = PriorityKey.SHION.priorityKey
        } else if (vtuberName.contains(VtuberName.AQUA.vtuberName2)) {
            preferencesKey = PriorityKey.AQUA.priorityKey
        } else if (vtuberName.contains(VtuberName.MARIN.vtuberName2)) {
            preferencesKey = PriorityKey.MARIN.priorityKey
        } else if (vtuberName.contains(VtuberName.LAMY.vtuberName2)) {
            preferencesKey = PriorityKey.LAMY.priorityKey
        } else if (vtuberName.contains(VtuberName.SUISEI.vtuberName2)) {
            preferencesKey = PriorityKey.SUISEI.priorityKey
        } else if (vtuberName.contains(VtuberName.PEKORA.vtuberName2)) {
            preferencesKey = PriorityKey.PEKORA.priorityKey
        } else if (vtuberName.contains(VtuberName.RUSHIA.vtuberName2)) {
            preferencesKey = PriorityKey.RUSHIA.priorityKey
        } else if (vtuberName.contains(VtuberName.MIKO.vtuberName2)) {
            preferencesKey = PriorityKey.MIKO.priorityKey
        } else if (vtuberName.contains(VtuberName.KANATA.vtuberName2)) {
            preferencesKey = PriorityKey.KANATA.priorityKey
        }

        var vtuberPriority = 1
        if (preferencesKey != null) {
            // Vtuberの優先度情報を取得する
            vtuberPriority = PreferencesUtils.loadPriorityInfo(context, preferencesKey)
        }

        // ライブタイトルの優先度とVtuberの優先度情報を足して返す
        return liveTitlePriority + vtuberPriority
    }
}