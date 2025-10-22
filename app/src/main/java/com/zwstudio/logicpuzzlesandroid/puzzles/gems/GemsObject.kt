package com.zwstudio.logicpuzzlesandroid.puzzles.gems

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class GemsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> GemsMarkerObject
            "pebble" -> GemsPebbleObject
            "gem" -> GemsGemObject()
            else -> GemsEmptyObject
        }
    }
}

object GemsEmptyObject : GemsObject()

class GemsHintObject(var state: HintState = HintState.Normal) : GemsObject()

object GemsMarkerObject : GemsObject() {
    override fun objAsString() = "marker"
}

object GemsPebbleObject : GemsObject() {
    override fun objAsString() = "pebble"
}

class GemsGemObject(var state: AllowedObjectState = AllowedObjectState.Normal) : GemsObject() {
    override fun objAsString() = "gem"
}

class GemsGameMove(val p: Position, var obj: GemsObject = GemsEmptyObject)
