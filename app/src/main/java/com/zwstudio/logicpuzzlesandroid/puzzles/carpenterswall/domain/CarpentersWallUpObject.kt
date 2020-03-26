package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class CarpentersWallUpObject : CarpentersWallObject() {
    var state = HintState.Normal
    override val isHint: Boolean
        get() = true

    override fun objAsString(): String {
        return "up"
    }
}