package com.zwstudio.logicpuzzlesandroid.puzzles.thecityrises

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class TheCityRisesObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> TheCityRisesMarkerObject
            "block" -> TheCityRisesBlockObject()
            else -> TheCityRisesEmptyObject
        }
    }
}

object TheCityRisesEmptyObject : TheCityRisesObject()

object TheCityRisesMarkerObject : TheCityRisesObject() {
    override fun objAsString() = "marker"
}

object TheCityRisesForbiddenObject : TheCityRisesObject() {
    override fun objAsString() = "forbidden"
}

class TheCityRisesBlockObject(var state: AllowedObjectState = AllowedObjectState.Normal) : TheCityRisesObject() {
    override fun objAsString() = "block"
}

class TheCityRisesGameMove(val p: Position, var obj: TheCityRisesObject = TheCityRisesEmptyObject)
