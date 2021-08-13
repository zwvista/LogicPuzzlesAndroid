package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class ParkLakesObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> ParkLakesMarkerObject
            "tree" -> ParkLakesTreeObject()
            else -> ParkLakesEmptyObject
        }
    }
}

object ParkLakesEmptyObject : ParkLakesObject()

class ParkLakesHintObject(var state: HintState = HintState.Normal, var tiles: Int = 0) : ParkLakesObject() {
    override fun objAsString() = "hint"
}

object ParkLakesMarkerObject : ParkLakesObject() {
    override fun objAsString() = "marker"
}

class ParkLakesTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : ParkLakesObject() {
    override fun objAsString() = "tree"
}

class ParkLakesGameMove(val p: Position, var obj: ParkLakesObject = ParkLakesEmptyObject)
