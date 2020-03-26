package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

open class HolidayIslandHintObject : HolidayIslandObject() {
    var state = HintState.Normal
    var tiles = 0
    override fun objAsString(): String {
        return "hint"
    }
}