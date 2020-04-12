package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

sealed class BalancedTapasObject {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "marker" -> BalancedTapasMarkerObject()
            "wall" -> BalancedTapasWallObject()
            else -> BalancedTapasEmptyObject()
        }
    }
}

class BalancedTapasEmptyObject : BalancedTapasObject()

class BalancedTapasHintObject(var state: HintState = HintState.Normal) : BalancedTapasObject() {
    override fun objTypeAsString() = "hint"
}

class BalancedTapasMarkerObject : BalancedTapasObject() {
    override fun objTypeAsString() = "marker"
}

class BalancedTapasWallObject(var state: HintState = HintState.Normal) : BalancedTapasObject() {
    override fun objTypeAsString() = "wall"
}

class BalancedTapasGameMove(val p: Position, var obj: BalancedTapasObject = BalancedTapasEmptyObject())
