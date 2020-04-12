package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class BattleShipsObject {
    Empty, Forbidden, Marker,
    BattleShipTop, BattleShipBottom, BattleShipLeft, BattleShipRight, BattleShipMiddle, BattleShipUnit;

    fun isShipPiece() = when (this) {
        Empty, Forbidden, Marker -> false
        else -> true
    }
}

class BattleShipsGameMove(val p: Position, var obj: BattleShipsObject = BattleShipsObject.Empty)
