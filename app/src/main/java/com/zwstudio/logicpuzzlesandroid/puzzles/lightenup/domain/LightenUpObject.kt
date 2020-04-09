package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

sealed class LightenUpObject(var lightness: Int = 0) {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "lightbulb" -> LightenUpLightbulbObject()
            "marker" -> LightenUpMarkerObject()
            else -> LightenUpEmptyObject()
        }
    }
}

class LightenUpEmptyObject : LightenUpObject()

class LightenUpLightbulbObject(var state: AllowedObjectState = AllowedObjectState.Normal) : LightenUpObject() {
    override fun objTypeAsString() = "lightbulb"
}

class LightenUpMarkerObject : LightenUpObject() {
    override fun objTypeAsString() = "marker"
}

class LightenUpWallObject(var state: HintState = HintState.Normal) : LightenUpObject() {
    override fun objTypeAsString() = "wall"
}

class LightenUpGameMove(val p: Position, var obj: LightenUpObject = LightenUpEmptyObject())
