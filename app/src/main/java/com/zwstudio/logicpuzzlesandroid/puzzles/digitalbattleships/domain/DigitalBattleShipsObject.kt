package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class DigitalBattleShipsObject {
    Empty, Forbidden, Marker, BattleShipTop, BattleShipBottom, BattleShipLeft, BattleShipRight, BattleShipMiddle, BattleShipUnit
}

class DigitalBattleShipsGameMove(val p: Position, var obj: DigitalBattleShipsObject = DigitalBattleShipsObject.Empty)
