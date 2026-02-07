package com.zwstudio.logicpuzzlesandroid.puzzles.castlepatrol

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class CastlePatrolObject(var lightness: Int = 0) {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "lightbulb" -> CastlePatrolLightbulbObject()
            "marker" -> CastlePatrolMarkerObject()
            else -> CastlePatrolEmptyObject()
        }
    }
}

class CastlePatrolEmptyObject : CastlePatrolObject()

class CastlePatrolLightbulbObject(var state: AllowedObjectState = AllowedObjectState.Normal) : CastlePatrolObject() {
    override fun objTypeAsString() = "lightbulb"
}

class CastlePatrolMarkerObject : CastlePatrolObject() {
    override fun objTypeAsString() = "marker"
}

class CastlePatrolWallObject(var state: HintState = HintState.Normal) : CastlePatrolObject() {
    override fun objTypeAsString() = "wall"
}

class CastlePatrolGameMove(val p: Position, var obj: CastlePatrolObject = CastlePatrolEmptyObject())
