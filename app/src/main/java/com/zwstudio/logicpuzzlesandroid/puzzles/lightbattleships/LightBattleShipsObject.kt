package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class LightBattleShipsObject {
    open fun objAsString() = "empty"

    fun isShipPiece() = when (this) {
        is LightBattleShipsEmptyObject, is LightBattleShipsForbiddenObject,
        is LightBattleShipsHintObject, is LightBattleShipsMarkerObject -> false
        else -> true
    }

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> LightBattleShipsMarkerObject
            "battleShipTop" -> LightBattleShipsBattleShipTopObject
            "battleShipBottom" -> LightBattleShipsBattleShipBottomObject
            "battleShipLeft" -> LightBattleShipsBattleShipLeftObject
            "battleShipRight" -> LightBattleShipsBattleShipRightObject
            "battleShipMiddle" -> LightBattleShipsBattleShipMiddleObject
            "battleShipUnit" -> LightBattleShipsBattleShipUnitObject
            else -> LightBattleShipsEmptyObject()
        }
    }
}

object LightBattleShipsBattleShipBottomObject : LightBattleShipsObject() {
    override fun objAsString() = "battleShipBottom"
}

object LightBattleShipsBattleShipLeftObject : LightBattleShipsObject() {
    override fun objAsString() = "battleShipLeft"
}

object LightBattleShipsBattleShipMiddleObject : LightBattleShipsObject() {
    override fun objAsString() = "battleShipMiddle"
}

object LightBattleShipsBattleShipRightObject : LightBattleShipsObject() {
    override fun objAsString() = "battleShipRight"
}

object LightBattleShipsBattleShipTopObject : LightBattleShipsObject() {
    override fun objAsString() = "battleShipTop"
}

object LightBattleShipsBattleShipUnitObject : LightBattleShipsObject() {
    override fun objAsString() = "battleShipUnit"
}

class LightBattleShipsEmptyObject : LightBattleShipsObject()


object LightBattleShipsForbiddenObject : LightBattleShipsObject() {
    override fun objAsString() = "forbidden"
}

class LightBattleShipsHintObject(var state: HintState = HintState.Normal) : LightBattleShipsObject() {
    override fun objAsString() = "hint"
}

object LightBattleShipsMarkerObject : LightBattleShipsObject() {
    override fun objAsString() = "marker"
}

class LightBattleShipsGameMove(val p: Position, var obj: LightBattleShipsObject = LightBattleShipsEmptyObject())
