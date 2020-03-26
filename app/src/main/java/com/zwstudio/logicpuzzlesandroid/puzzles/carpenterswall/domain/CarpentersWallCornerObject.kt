package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

open class CarpentersWallCornerObject : CarpentersWallObject() {
    var state = HintState.Normal
    var tiles = 0
    override val isHint: Boolean
        get() = true

    override fun objAsString(): String {
        return "corner"
    }
}