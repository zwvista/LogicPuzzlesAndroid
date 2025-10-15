package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class HolidayIslandObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> HolidayIslandMarkerObject
            "water" -> HolidayIslandWaterObject()
            else -> HolidayIslandEmptyObject
        }
    }
}

object HolidayIslandEmptyObject : HolidayIslandObject()

object HolidayIslandForbiddenObject : HolidayIslandObject() {
    override fun objAsString() = "forbidden"

}

class HolidayIslandHintObject(var state: HintState = HintState.Normal, var tiles: Int = 0) : HolidayIslandObject() {
    override fun objAsString() = "hint"
}

object HolidayIslandMarkerObject : HolidayIslandObject() {
    override fun objAsString() = "marker"
}

class HolidayIslandWaterObject(var state: AllowedObjectState = AllowedObjectState.Normal) : HolidayIslandObject() {
    override fun objAsString() = "water"
}

class HolidayIslandGameMove(val p: Position, var obj: HolidayIslandObject = HolidayIslandEmptyObject)
