package com.zwstudio.logicpuzzlesandroid.puzzles.inbetweensumscrapers

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class InbetweenSumscrapersObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> InbetweenSumscrapersMarkerObject
            "tower" -> InbetweenSumscrapersPostObject()
            else -> InbetweenSumscrapersMarkerObject
        }
    }
}

object InbetweenSumscrapersEmptyObject : InbetweenSumscrapersObject()

object InbetweenSumscrapersForbiddenObject : InbetweenSumscrapersObject() {
    override fun objAsString() = "forbidden"
}

object InbetweenSumscrapersMarkerObject : InbetweenSumscrapersObject() {
    override fun objAsString() = "marker"
}

class InbetweenSumscrapersPostObject(var state: AllowedObjectState = AllowedObjectState.Normal) : InbetweenSumscrapersObject() {
    override fun objAsString() = "tower"
}

class InbetweenSumscrapersGameMove(val p: Position, var obj: InbetweenSumscrapersObject = InbetweenSumscrapersEmptyObject)
