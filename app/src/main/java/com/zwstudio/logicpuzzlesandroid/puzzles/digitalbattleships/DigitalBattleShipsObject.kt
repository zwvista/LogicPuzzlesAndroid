package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class DigitalBattleShipsObject {
    Empty, Forbidden, Marker,
    BattleShipTop, BattleShipBottom, BattleShipLeft, BattleShipRight, BattleShipMiddle, BattleShipUnit;

    val isShipPiece get() =
        listOf(BattleShipTop, BattleShipBottom, BattleShipLeft, BattleShipRight, BattleShipMiddle, BattleShipUnit).contains(this)
}

class DigitalBattleShipsGameMove(val p: Position, var obj: DigitalBattleShipsObject = DigitalBattleShipsObject.Empty)
