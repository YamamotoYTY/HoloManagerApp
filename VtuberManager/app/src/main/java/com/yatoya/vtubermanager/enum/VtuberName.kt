package com.yatoya.vtubermanager.enum

/**
 * Vtuberの名前
 * @param vtuberName1 ホロライブスクレイピング用の名前
 * @param vtuberName2 Youtubeのチャンネル名の一部
 */
enum class VtuberName(val vtuberName1: String, val vtuberName2: String) {
    // 0期生
    SUISEI("星街すいせい", "Suisei Channel"),
    MIKO("さくらみこ", "さくらみこ"),
    SORA("ときのそら", "ときのそら"),
    ROBOKO("ロボ子", "ロボ子"),

    // 1期生
    FUBUKI("白上フブキ", "白上フブキ"),
    MATSURI("夏色まつり", "夏色まつり"),
    HAACHAMA("赤井はあと", "赤井はあと"),
    MERU("夜空メル", "夜空メル"),
    AKIROZE("アキロゼ", "アキロゼ"),

    // 2期生
    SHION("紫咲シオン", "紫咲シオン"),
    AQUA("湊あくあ", "湊あくあ"),
    AYAME("百鬼あやめ", "百鬼あやめ"),
    SUBARU("大空スバル", "大空スバル"),
    CHOKO("癒月ちょこ", "癒月ちょこ"),

    // ゲーマーズ
    KORONE("戌神ころね", "戌神ころね"),
    OKAYU("猫又おかゆ", "猫又おかゆ"),
    MIO("大神ミオ", "大神ミオ"),

    // 3期生
    MARIN("宝鐘マリン", "宝鐘マリン"),
    PEKORA("兎田ぺこら", "兎田ぺこら"),
    RUSHIA("潤羽るしあ", "潤羽るしあ"),
    NOERU("白銀ノエル", "白銀ノエル"),
    FUREA("不知火フレア", "不知火フレア"),

    // 4期生
    KANATA("天音かなた", "天音かなた"),
    TOWA("常闇トワ", "常闇トワ"),
    WATAME("角巻わため", "角巻わため"),
    LUNA("姫森ルーナ", "姫森ルーナ"),
    COCO("桐生ココ", "桐生ココ"),

    // 5期生
    LAMY("雪花ラミィ", "雪花ラミィ"),
    PORUKA("尾丸ポルカ", "尾丸ポルカ"),
    BOTAN("獅白ぼたん", "獅白ぼたん"),
    NENE("桃鈴ねね", "桃鈴ねね")
}