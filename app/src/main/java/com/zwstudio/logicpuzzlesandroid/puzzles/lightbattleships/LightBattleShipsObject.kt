package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class LightBattleShipsObject {
    Empty, Forbidden, Marker, Hint,
    BattleShipTop, BattleShipBottom, BattleShipLeft, BattleShipRight, BattleShipMiddle, BattleShipUnit;

    val isShipPiece get() =
        listOf(BattleShipTop, BattleShipBottom, BattleShipLeft, BattleShipRight, BattleShipMiddle, BattleShipUnit).contains(this)
}

class LightBattleShipsGameMove(val p: Position, var obj: LightBattleShipsObject = LightBattleShipsObject.Empty)
