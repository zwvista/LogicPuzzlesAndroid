package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class DigitalBattleShipsObject {
    Empty, Forbidden, Marker,
    BattleShipTop, BattleShipBottom, BattleShipLeft, BattleShipRight, BattleShipMiddle, BattleShipUnit;

    fun isShipPiece() = when (this) {
        Empty, Forbidden, Marker -> false
        else -> true
    }
}

class DigitalBattleShipsGameMove(val p: Position, var obj: DigitalBattleShipsObject = DigitalBattleShipsObject.Empty)
