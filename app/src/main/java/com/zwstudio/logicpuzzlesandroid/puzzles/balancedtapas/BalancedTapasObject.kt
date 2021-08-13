package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class BalancedTapasObject {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "marker" -> BalancedTapasMarkerObject
            "wall" -> BalancedTapasWallObject()
            else -> BalancedTapasEmptyObject
        }
    }
}

object BalancedTapasEmptyObject : BalancedTapasObject()

class BalancedTapasHintObject(var state: HintState = HintState.Normal) : BalancedTapasObject() {
    override fun objTypeAsString() = "hint"
}

object BalancedTapasMarkerObject : BalancedTapasObject() {
    override fun objTypeAsString() = "marker"
}

class BalancedTapasWallObject(var state: HintState = HintState.Normal) : BalancedTapasObject() {
    override fun objTypeAsString() = "wall"
}

class BalancedTapasGameMove(val p: Position, var obj: BalancedTapasObject = BalancedTapasEmptyObject)
