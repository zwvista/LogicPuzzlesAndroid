package com.zwstudio.logicpuzzlesandroid.puzzles.archipelago

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class ArchipelagoObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> ArchipelagoMarkerObject
            "water" -> ArchipelagoWaterObject()
            else -> ArchipelagoEmptyObject
        }
    }
}

object ArchipelagoEmptyObject : ArchipelagoObject()

class ArchipelagoHintObject(var state: HintState = HintState.Normal) : ArchipelagoObject() {
    override fun objAsString() = "hint"
}

object ArchipelagoMarkerObject : ArchipelagoObject() {
    override fun objAsString() = "marker"
}

class ArchipelagoWaterObject(var state: AllowedObjectState = AllowedObjectState.Normal) : ArchipelagoObject() {
    override fun objAsString() = "water"
}

class ArchipelagoGameMove(val p: Position, var obj: ArchipelagoObject = ArchipelagoEmptyObject)
