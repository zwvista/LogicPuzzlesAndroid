package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LightBattleShipsGame(layout: List<String>, gi: GameInterface<LightBattleShipsGame, LightBattleShipsGameMove, LightBattleShipsGameState>, gdi: GameDocumentInterface) : CellsGame<LightBattleShipsGame, LightBattleShipsGameMove, LightBattleShipsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Directions8
    }

    val pos2hint = mutableMapOf<Position, Int>()
    var pos2obj = mutableMapOf<Position, LightBattleShipsObject>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch.isDigit()) {
                    pos2hint[p] = ch - '0'
                    pos2obj[p] = LightBattleShipsObject.Hint
                } else
                    pos2obj[p] = when (ch) {
                        '^' -> LightBattleShipsObject.BattleShipTop
                        'v' -> LightBattleShipsObject.BattleShipBottom
                        '<' -> LightBattleShipsObject.BattleShipLeft
                        '>' -> LightBattleShipsObject.BattleShipRight
                        '+' -> LightBattleShipsObject.BattleShipMiddle
                        'o' -> LightBattleShipsObject.BattleShipUnit
                        '.' -> LightBattleShipsObject.Marker
                        else -> continue
                    }
            }
        }
        val state = LightBattleShipsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}