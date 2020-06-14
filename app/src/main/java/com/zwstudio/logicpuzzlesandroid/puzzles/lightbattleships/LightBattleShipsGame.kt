package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LightBattleShipsGame(layout: List<String>, gi: GameInterface<LightBattleShipsGame, LightBattleShipsGameMove, LightBattleShipsGameState>, gdi: GameDocumentInterface) : CellsGame<LightBattleShipsGame, LightBattleShipsGameMove, LightBattleShipsGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1)
        )
    }

    var pos2hint = mutableMapOf<Position, Int>()
    var pos2obj = mutableMapOf<Position, LightBattleShipsObject>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (val ch = str[c]) {
                    '^' -> pos2obj[p] = LightBattleShipsBattleShipTopObject()
                    'v' -> pos2obj[p] = LightBattleShipsBattleShipBottomObject()
                    '<' -> pos2obj[p] = LightBattleShipsBattleShipLeftObject()
                    '>' -> pos2obj[p] = LightBattleShipsBattleShipRightObject()
                    '+' -> pos2obj[p] = LightBattleShipsBattleShipMiddleObject()
                    'o' -> pos2obj[p] = LightBattleShipsBattleShipUnitObject()
                    '.' -> pos2obj[p] = LightBattleShipsMarkerObject()
                    in '0'..'9' -> pos2hint[Position(r, c)] = ch - '0'
                }
            }
        }
        val state = LightBattleShipsGameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}