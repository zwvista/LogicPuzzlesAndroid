package com.zwstudio.logicpuzzlesandroid.puzzles.mineships

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class MineShipsObject {
    Empty, Forbidden, Marker, Hint,
    BattleShipTop, BattleShipBottom, BattleShipLeft, BattleShipRight, BattleShipMiddle, BattleShipUnit;

    val isShipPiece get() =
        listOf(BattleShipTop, BattleShipBottom, BattleShipLeft, BattleShipRight, BattleShipMiddle, BattleShipUnit).contains(this)
}

class MineShipsGameMove(val p: Position, var obj: MineShipsObject = MineShipsObject.Empty)
