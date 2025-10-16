package com.zwstudio.logicpuzzlesandroid.puzzles.thermometers

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ThermometersGame(layout: List<String>, val onlyOneArrow: Boolean, gi: GameInterface<ThermometersGame, ThermometersGameMove, ThermometersGameState>, gdi: GameDocumentInterface) : CellsGame<ThermometersGame, ThermometersGameMove, ThermometersGameState>(gi, gdi) {
    companion object {
        var offset = Position.Directions4
        var offset2 = Position.Directions8
    }

    var row2hint: IntArray
    var col2hint: IntArray
    var pos2arrow = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        row2hint = IntArray(rows)
        col2hint = IntArray(cols)
        for (r in 0 until rows + 1) {
            val str = layout[r]
            for (c in 0 until cols + 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ') {
                    val n = ch - '0'
                    if (r == rows)
                        col2hint[c] = n
                    else if (c == cols)
                        row2hint[r] = n
                    else
                        pos2arrow[p] = n
                }
            }
        }
        val state = ThermometersGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getRowState(row: Int) = currentState.row2state[row]
    fun getColState(col: Int) = currentState.col2state[col]
}