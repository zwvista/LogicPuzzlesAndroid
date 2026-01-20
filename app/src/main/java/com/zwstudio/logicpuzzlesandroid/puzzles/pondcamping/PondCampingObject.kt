package com.zwstudio.logicpuzzlesandroid.puzzles.pondcamping

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class PondCampingObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> PondCampingMarkerObject
            "water" -> PondCampingWaterObject()
            else -> PondCampingEmptyObject
        }
    }
}

object PondCampingEmptyObject : PondCampingObject()

object PondCampingForbiddenObject : PondCampingObject() {
    override fun objAsString() = "forbidden"

}

class PondCampingHintObject(var state: HintState = HintState.Normal, var tiles: Int = 0) : PondCampingObject() {
    override fun objAsString() = "hint"
}

object PondCampingMarkerObject : PondCampingObject() {
    override fun objAsString() = "marker"
}

class PondCampingWaterObject(var state: AllowedObjectState = AllowedObjectState.Normal) : PondCampingObject() {
    override fun objAsString() = "water"
}

class PondCampingGameMove(val p: Position, var obj: PondCampingObject = PondCampingEmptyObject)
