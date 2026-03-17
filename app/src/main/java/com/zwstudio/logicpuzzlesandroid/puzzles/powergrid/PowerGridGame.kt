package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PowerGridGame(layout: List<String>, gi: GameInterface<PowerGridGame, PowerGridGameMove, PowerGridGameState>, gdi: GameDocumentInterface) : CellsGame<PowerGridGame, PowerGridGameMove, PowerGridGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    var row2hint: IntArray
    var col2hint: IntArray

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        row2hint = IntArray(rows)
        col2hint = IntArray(cols)
        for (r in 0 until rows + 1) {
            val str = layout[r]
            for (c in 0 until cols + 1) {
                val isHintRow = r == rows
                val isHintCol = c == cols
                if (isHintRow == isHintCol) continue
                val ch = str[c]
                val n = if (ch == ' ') -1 else ch - '0'
                if (isHintRow)
                    col2hint[c] = n
                else
                    row2hint[r] = n
            }
        }
        val state = PowerGridGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun row2state(row: Int) = currentState.row2state[row]
    fun col2state(col: Int) = currentState.col2state[col]
}
