package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class LighthousesHintObject : LighthousesObject() {
    var state = HintState.Normal
    override fun objAsString(): String {
        return "hint"
    }
}