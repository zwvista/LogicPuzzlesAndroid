package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class TierraDelFuegoObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> TierraDelFuegoMarkerObject
            "tree" -> TierraDelFuegoTreeObject()
            else -> TierraDelFuegoEmptyObject
        }
    }
}

object TierraDelFuegoEmptyObject : TierraDelFuegoObject()

object TierraDelFuegoForbiddenObject : TierraDelFuegoObject() {
    override fun objAsString() = "forbidden"
}

class TierraDelFuegoHintObject(var state: HintState = HintState.Normal, var id: Char = '0') : TierraDelFuegoObject() {
    override fun objAsString() = "hint"
}

object TierraDelFuegoMarkerObject : TierraDelFuegoObject() {
    override fun objAsString() = "marker"
}

class TierraDelFuegoTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : TierraDelFuegoObject() {
    override fun objAsString() = "tree"
}

class TierraDelFuegoGameMove(val p: Position, var obj: TierraDelFuegoObject = TierraDelFuegoEmptyObject)
