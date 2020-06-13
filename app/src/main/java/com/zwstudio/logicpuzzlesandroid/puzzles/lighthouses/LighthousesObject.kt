package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

abstract class LighthousesObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> LighthousesMarkerObject()
            "lighthouse" -> LighthousesLighthouseObject()
            else -> LighthousesMarkerObject()
        }
    }
}

class LighthousesEmptyObject : LighthousesObject()

class LighthousesForbiddenObject : LighthousesObject() {
    override fun objAsString() = "forbidden"
}

class LighthousesHintObject(var state: HintState = HintState.Normal) : LighthousesObject() {
    override fun objAsString() = "hint"
}

class LighthousesLighthouseObject(var state: AllowedObjectState = AllowedObjectState.Normal) : LighthousesObject() {
    override fun objAsString() = "lighthouse"
}

class LighthousesMarkerObject : LighthousesObject() {
    override fun objAsString() = "marker"
}

class LighthousesGameMove(val p: Position, var obj: LighthousesObject = LighthousesEmptyObject())
