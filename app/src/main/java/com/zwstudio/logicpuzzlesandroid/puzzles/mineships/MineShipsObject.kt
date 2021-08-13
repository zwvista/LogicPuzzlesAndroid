package com.zwstudio.logicpuzzlesandroid.puzzles.mineships

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class MineShipsObject {
    open fun objAsString() = "empty"

    fun isShipPiece() = when (this) {
        is MineShipsEmptyObject, is MineShipsForbiddenObject, is MineShipsMarkerObject -> false
        else -> true
    }

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> MineShipsMarkerObject
            "battleShipTop" -> MineShipsBattleShipTopObject
            "battleShipBottom" -> MineShipsBattleShipBottomObject
            "battleShipLeft" -> MineShipsBattleShipLeftObject
            "battleShipRight" -> MineShipsBattleShipRightObject
            "battleShipMiddle" -> MineShipsBattleShipMiddleObject
            "battleShipUnit" -> MineShipsBattleShipUnitObject
            else -> MineShipsEmptyObject
        }
    }
}

object MineShipsBattleShipBottomObject : MineShipsObject() {
    override fun objAsString() = "battleShipBottom"
}

object MineShipsBattleShipLeftObject : MineShipsObject() {
    override fun objAsString() = "battleShipLeft"
}

object MineShipsBattleShipMiddleObject : MineShipsObject() {
    override fun objAsString() = "battleShipMiddle"
}

object MineShipsBattleShipRightObject : MineShipsObject() {
    override fun objAsString() = "battleShipRight"
}

object MineShipsBattleShipTopObject : MineShipsObject() {
    override fun objAsString() = "battleShipTop"
}

object MineShipsBattleShipUnitObject : MineShipsObject() {
    override fun objAsString() = "battleShipUnit"
}

object MineShipsEmptyObject : MineShipsObject() {
    override fun objAsString() = "empty"
}

object MineShipsForbiddenObject : MineShipsObject()

class MineShipsHintObject(var state: HintState = HintState.Normal) : MineShipsObject() {
    override fun objAsString() = "hint"
}

object MineShipsMarkerObject : MineShipsObject() {
    override fun objAsString() = "marker"
}

class MineShipsGameMove(val p: Position, var obj: MineShipsObject = MineShipsEmptyObject)
