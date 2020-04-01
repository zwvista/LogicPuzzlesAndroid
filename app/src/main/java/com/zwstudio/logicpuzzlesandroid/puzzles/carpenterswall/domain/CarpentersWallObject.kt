package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

sealed class CarpentersWallObject(val isHint: Boolean = false) {
    open fun objAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String?): CarpentersWallObject {
            return when (str) {
                "wall" -> CarpentersWallWallObject()
                "marker" -> CarpentersWallMarkerObject()
                else -> CarpentersWallEmptyObject()
            }
        }
    }
}

class CarpentersWallCornerObject(var state: HintState = HintState.Normal, var tiles: Int = 0) : CarpentersWallObject(true) {
    override fun objAsString() = "corner"
}

class CarpentersWallDownObject(var state: HintState = HintState.Normal) : CarpentersWallObject(true) {
    override fun objAsString() = "down"
}

class CarpentersWallEmptyObject : CarpentersWallObject()

class CarpentersWallLeftObject(var state: HintState = HintState.Normal) : CarpentersWallObject(true) {
    override fun objAsString() = "left"
}

class CarpentersWallMarkerObject : CarpentersWallObject() {
    override fun objAsString() = "marker"
}

class CarpentersWallRightObject(var state: HintState = HintState.Normal) : CarpentersWallObject(true) {
    override fun objAsString() =  "right"
}

class CarpentersWallUpObject(var state: HintState = HintState.Normal) : CarpentersWallObject(true) {
    override fun objAsString() = "up"
}

class CarpentersWallWallObject : CarpentersWallObject() {
    override fun objAsString() = "wall"
}

class CarpentersWallGameMove(val p: Position, var obj: CarpentersWallObject)
