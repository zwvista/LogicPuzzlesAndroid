package com.zwstudio.logicpuzzlesandroid.puzzles.desertdunes

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class DesertDunesObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> DesertDunesMarkerObject
            "dune" -> DesertDunesDuneObject()
            else -> DesertDunesEmptyObject
        }
    }
}

object DesertDunesEmptyObject : DesertDunesObject()

class DesertDunesHintObject(var state: HintState = HintState.Normal) : DesertDunesObject() {
    override fun objAsString() = "hint"
}

object DesertDunesMarkerObject : DesertDunesObject() {
    override fun objAsString() = "marker"
}

object DesertDunesForbiddenObject : DesertDunesObject() {
    override fun objAsString() = "forbidden"
}

class DesertDunesDuneObject(var state: AllowedObjectState = AllowedObjectState.Normal) : DesertDunesObject() {
    override fun objAsString() = "dune"
}

class DesertDunesGameMove(val p: Position, var obj: DesertDunesObject = DesertDunesEmptyObject)
