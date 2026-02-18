package com.zwstudio.logicpuzzlesandroid.puzzles.battleships

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class BattleShipsObject {
    Empty, Forbidden, Marker,
    BattleShipTop, BattleShipBottom, BattleShipLeft, BattleShipRight, BattleShipMiddle, BattleShipUnit;

    val isShipPiece get() =
        listOf(BattleShipTop, BattleShipBottom, BattleShipLeft, BattleShipRight, BattleShipMiddle, BattleShipUnit).contains(this)
}

class BattleShipsGameMove(val p: Position, var obj: BattleShipsObject = BattleShipsObject.Empty)
