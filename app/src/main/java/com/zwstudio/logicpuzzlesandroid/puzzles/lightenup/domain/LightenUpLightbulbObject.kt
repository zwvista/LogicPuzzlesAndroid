package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState

class LightenUpLightbulbObject : LightenUpObject() {
    var state = AllowedObjectState.Normal
    override fun objTypeAsString(): String {
        return "lightbulb"
    }
}