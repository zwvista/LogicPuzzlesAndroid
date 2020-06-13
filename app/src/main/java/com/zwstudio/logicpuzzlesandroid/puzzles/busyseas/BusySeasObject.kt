package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class BusySeasObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> BusySeasMarkerObject()
            "lighthouse" -> BusySeasLighthouseObject()
            else -> BusySeasMarkerObject()
        }
    }
}

class BusySeasEmptyObject : BusySeasObject()

class BusySeasForbiddenObject : BusySeasObject() {
    override fun objAsString() = "forbidden"
}

class BusySeasHintObject(var state: HintState = HintState.Normal) : BusySeasObject() {
    override fun objAsString() = "hint"
}

class BusySeasLighthouseObject(var state: AllowedObjectState = AllowedObjectState.Normal) : BusySeasObject() {
    override fun objAsString() = "lighthouse"
}

class BusySeasMarkerObject : BusySeasObject() {
    override fun objAsString() = "marker"
}

class BusySeasGameMove(val p: Position, var obj: BusySeasObject = BusySeasEmptyObject())
