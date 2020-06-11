package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

sealed class ParkLakesObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> ParkLakesMarkerObject()
            "tree" -> ParkLakesTreeObject()
            else -> ParkLakesEmptyObject()
        }
    }
}

class ParkLakesEmptyObject : ParkLakesObject()

class ParkLakesHintObject(var state: HintState = HintState.Normal, var tiles: Int = 0) : ParkLakesObject() {
    override fun objAsString() = "hint"
}

class ParkLakesMarkerObject : ParkLakesObject() {
    override fun objAsString() = "marker"
}

class ParkLakesTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : ParkLakesObject() {
    override fun objAsString() = "tree"
}

class ParkLakesGameMove(val p: Position, var obj: ParkLakesObject = ParkLakesEmptyObject())
