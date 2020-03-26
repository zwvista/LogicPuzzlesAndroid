package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState

class LighthousesLighthouseObject : LighthousesObject() {
    var state = AllowedObjectState.Normal
    override fun objAsString(): String {
        return "lighthouse"
    }
}