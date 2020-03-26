package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class LightenUpWallObject : LightenUpObject() {
    var state = HintState.Normal
    override fun objTypeAsString(): String {
        return "wall"
    }
}