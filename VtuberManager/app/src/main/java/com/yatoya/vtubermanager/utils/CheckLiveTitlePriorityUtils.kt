package com.yatoya.vtubermanager.utils

import com.yatoya.vtubermanager.enum.LiveTitleType

class CheckLiveTitlePriorityUtils {

    fun checkLiveTitlePriority(liveTitle: String): Int {
        var priority = 1

        // NOTE:: 優先度5
        if (liveTitle.contains(LiveTitleType.SONG1.title) || liveTitle.contains(LiveTitleType.SONG2.title)) {
            // 歌枠の場合
            priority = 5
        } else if (liveTitle.contains(LiveTitleType.ANNOUNCEMENT1.title)
            || liveTitle.contains(LiveTitleType.ANNOUNCEMENT2.title)
            || liveTitle.contains(LiveTitleType.ANNOUNCEMENT3.title)
        ) {
            // お披露目の場合
            priority = 5
        } else if (liveTitle.contains(LiveTitleType.MEMORY1.title)
            || liveTitle.contains(LiveTitleType.MEMORY2.title)
            || liveTitle.contains(LiveTitleType.MEMORY3.title)
        ) {
            // 記念枠の場合
            priority = 5
        } else if (liveTitle.contains(LiveTitleType.MEMBER1.title)
            || liveTitle.contains(LiveTitleType.MEMBER2.title)
        ) {
            // メン限の場合
            priority = 5
        } else if (liveTitle.contains(LiveTitleType.MARIO_CART1.title)
            || liveTitle.contains(LiveTitleType.MARIO_CART2.title)
        ) {
            // マリカの場合
            priority = 5
        } else if (liveTitle.contains(LiveTitleType.AQUSHIO.title)) {
            // あくシオの場合
            priority = 5
        }
        // NOTE:: 優先度4
        else if (liveTitle.contains(LiveTitleType.MICRA1.title)
            || liveTitle.contains(LiveTitleType.MICRA2.title)
            || liveTitle.contains(LiveTitleType.MICRA3.title)
        ) {
            // マイクラの場合
            priority = 4
        } else if (liveTitle.contains(LiveTitleType.SUMABURA1.title)
            || liveTitle.contains(LiveTitleType.SUMABURA2.title)
            || liveTitle.contains(LiveTitleType.SUMABURA3.title)
            || liveTitle.contains(LiveTitleType.SUMABURA4.title)
        ) {
            // スマブラの場合
            priority = 4
        } else if (liveTitle.contains(LiveTitleType.CHAT_TALK1.title)
            || liveTitle.contains(LiveTitleType.CHAT_TALK2.title)
            || liveTitle.contains(LiveTitleType.CHAT_TALK3.title)
        ) {
            // 雑談・晩酌の場合
            priority = 4
        } else if (liveTitle.contains(LiveTitleType.AMONG_US1.title)
            || liveTitle.contains(LiveTitleType.AMONG_US2.title)
            || liveTitle.contains(LiveTitleType.AMONG_US3.title)
        ) {
            // Among Usの場合
            priority = 4
        } else if (liveTitle.contains(LiveTitleType.DRAGON_YAKUZA1.title)) {
            // 龍が如くの場合
            priority = 4
        } else if (liveTitle.contains(LiveTitleType.WITH_VIEWING.title)) {
            // 同時視聴の場合
            priority = 4
        }
        // NOTE:: 優先度3
        else if (liveTitle.contains(LiveTitleType.HORROR.title)) {
            // ホラーゲームの場合
            priority = 3
        } else if (liveTitle.contains(LiveTitleType.OFF_COLLABO1.title)
            || liveTitle.contains(LiveTitleType.OFF_COLLABO2.title)
        ) {
            // オフコラボの場合
            priority = 3
        } else if (liveTitle.contains(LiveTitleType.ASOBI_TAIZEN.title)) {
            // アソビ大全の場合
            priority = 3
        } else if (liveTitle.contains(LiveTitleType.GTA.title)) {
            // GTAの場合
            priority = 3
        } else if (liveTitle.contains(LiveTitleType.RING_FIT.title)) {
            // リングフィットの場合
            priority = 3
        }
        // NOTE:: 優先度2
        else if (liveTitle.contains(LiveTitleType.FALL_GUYS.title)) {
            // Fall Guysの場合
            priority = 2
        } else if (liveTitle.contains(LiveTitleType.MORRY1.title)
            || liveTitle.contains(LiveTitleType.MORRY2.title)
        ) {
            // モーリーオンラインの場合
            priority = 2
        } else if (liveTitle.contains(LiveTitleType.TUBO_OJI.title)) {
            // 壺おじの場合
            priority = 2
        } else if (liveTitle.contains(LiveTitleType.SLITHER.title)) {
            // SLITHER(蛇)ゲームの場合
            priority = 2
        }
        // NOTE:: 優先度1
        else if (liveTitle.contains(LiveTitleType.PASSPARTOUT.title)) {
            // PASSPARTOUT(お絵かき)ゲームの場合
            priority = 1
        } else if (liveTitle.contains(LiveTitleType.ARK.title)) {
            // ARKの場合
            priority = 1
        } else if (liveTitle.contains(LiveTitleType.APEX.title)) {
            // APEXの場合
            priority = 1
        }

        return priority
    }
}