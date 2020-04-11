package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class BattleShipsObject {
    Empty, Forbidden, Marker,
    BattleShipTop, BattleShipBottom, BattleShipLeft, BattleShipRight, BattleShipMiddle, BattleShipUnit
}

class BattleShipsGameMove(val p: Position, var obj: BattleShipsObject = BattleShipsObject.Empty)

