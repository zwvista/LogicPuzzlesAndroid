package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState

class HolidayIslandTreeObject : HolidayIslandObject() {
    var state = AllowedObjectState.Normal
    override fun objAsString(): String {
        return "tree"
    }
}