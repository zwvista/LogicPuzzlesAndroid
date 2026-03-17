package com.zwstudio.logicpuzzlesandroid.puzzles.battleships

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BattleShipsGame(layout: List<String>, gi: GameInterface<BattleShipsGame, BattleShipsGameMove, BattleShipsGameState>, gdi: GameDocumentInterface) : CellsGame<BattleShipsGame, BattleShipsGameMove, BattleShipsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Directions8
    }

    var row2hint: IntArray
    var col2hint: IntArray
    var pos2obj = mutableMapOf<Position, BattleShipsObject>()

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        row2hint = IntArray(rows)
        col2hint = IntArray(cols)

        for (r in 0 until rows + 1) {
            val str = layout[r]
            for (c in 0 until cols + 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch.isDigit()) {
                    val n = ch - '0'
                    if (r == rows)
                        col2hint[c] = n
                    else if (c == cols)
                        row2hint[r] = n
                } else
                    pos2obj[p] = when (ch) {
                        '^' -> BattleShipsObject.BattleShipTop
                        'v' -> BattleShipsObject.BattleShipBottom
                        '<' -> BattleShipsObject.BattleShipLeft
                        '>' -> BattleShipsObject.BattleShipRight
                        '+' -> BattleShipsObject.BattleShipMiddle
                        'o' -> BattleShipsObject.BattleShipUnit
                        '.' -> BattleShipsObject.Marker
                        else -> continue
                    }
            }
        }

        val state = BattleShipsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun row2state(row: Int) = currentState.row2state[row]
    fun col2state(col: Int) = currentState.col2state[col]
}
