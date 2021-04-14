package com.yatoya.vtubermanager.enum

/**
 * 優先度の値
 */
enum class PriorityValue(val value: Int) {
    // 7以上を基準とする
    HIGH(7),

    // 4以上7未満を基準とする
    MIDDLE(4),

    // 3以下を基準とする
    LOW(3),
}