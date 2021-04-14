package com.yatoya.vtubermanager.enum

/**
 * ライブタイトルタイプ
 */
enum class LiveTitleType(val title: String) {
    // NOTE:: 優先度5↓
    SONG1("歌枠"),
    SONG2("歌"),

    ANNOUNCEMENT1("お披露目"),
    ANNOUNCEMENT2("新衣装"),
    ANNOUNCEMENT3("3D"),

    MEMORY1("記念"),
    MEMORY2("万人"),
    MEMORY3("誕生"),

    MEMBER1("メンバー限定"),
    MEMBER2("メン限"),

    MARIO_CART1("マリオカート"),
    MARIO_CART2("マリカ"),

    AQUSHIO("あくしお"),

    // NOTE:: 優先度4↓
    MICRA1("マイクラ"),
    MICRA2("Minecraft"),
    MICRA3("AKUKIN"),

    SUMABURA1("スマブラ"),
    SUMABURA2("大乱闘"),
    SUMABURA3("スマッシュブラザーズ"),
    SUMABURA4("Super Smash"),

    CHAT_TALK1("雑談"),
    CHAT_TALK2("晩酌"),
    CHAT_TALK3("Talk"),

    AMONG_US1("AmongUs"),
    AMONG_US2("Among Us"),
    AMONG_US3("宇宙人狼"),

    DRAGON_YAKUZA1("龍が如く"),

    WITH_VIEWING("同時視聴"),

    // NOTE:: 優先度3↓
    HORROR("ホラー"),

    OFF_COLLABO1("オフコラボ"),
    OFF_COLLABO2("オフ"),

    ASOBI_TAIZEN("アソビ大全"),

    GTA("GTA"),

    RING_FIT("リングフィット"),

    // NOTE:: 優先度2↓
    FALL_GUYS("Fall guys"),

    MORRY1("トレバ"),
    MORRY2("モーリーオンライン"),

    TUBO_OJI("壺おじ"),

    SLITHER("slither"),

    // NOTE:: 優先度1↓
    PASSPARTOUT("Passpartout"),

    ARK("ARK"),

    APEX("APEX")
}