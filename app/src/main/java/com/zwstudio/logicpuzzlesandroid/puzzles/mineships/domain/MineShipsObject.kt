package com.zwstudio.logicpuzzlesandroid.puzzles.mineships.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

sealed class MineShipsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> MineShipsMarkerObject()
            "battleShipTop" -> MineShipsBattleShipTopObject()
            "battleShipBottom" -> MineShipsBattleShipBottomObject()
            "battleShipLeft" -> MineShipsBattleShipLeftObject()
            "battleShipRight" -> MineShipsBattleShipRightObject()
            "battleShipMiddle" -> MineShipsBattleShipMiddleObject()
            "battleShipUnit" -> MineShipsBattleShipUnitObject()
            else -> MineShipsEmptyObject()
        }
    }
}

class MineShipsBattleShipBottomObject : MineShipsObject() {
    override fun objAsString() = "battleShipBottom"
}

class MineShipsBattleShipLeftObject : MineShipsObject() {
    override fun objAsString() = "battleShipLeft"
}

class MineShipsBattleShipMiddleObject : MineShipsObject() {
    override fun objAsString() = "battleShipMiddle"
}

class MineShipsBattleShipRightObject : MineShipsObject() {
    override fun objAsString() = "battleShipRight"
}

class MineShipsBattleShipTopObject : MineShipsObject() {
    override fun objAsString() = "battleShipTop"
}

class MineShipsBattleShipUnitObject : MineShipsObject() {
    override fun objAsString() = "battleShipUnit"
}

class MineShipsEmptyObject : MineShipsObject() {
    override fun objAsString() = "empty"
}

class MineShipsForbiddenObject : MineShipsObject()

class MineShipsHintObject(var state: HintState = HintState.Normal) : MineShipsObject() {
    override fun objAsString() = "hint"
}

class MineShipsMarkerObject : MineShipsObject() {
    override fun objAsString() = "marker"
}

class MineShipsGameMove(val p: Position, var obj: MineShipsObject = MineShipsEmptyObject())
