package com.zwstudio.logicpuzzlesandroid.puzzles.tents

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TentsGame(layout: List<String>, gi: GameInterface<TentsGame, TentsGameMove, TentsGameState>, gdi: GameDocumentInterface) : CellsGame<TentsGame, TentsGameMove, TentsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Directions8
        const val PUZ_UNKNOWN = -1
    }

    val row2hint: IntArray
    val col2hint: IntArray
    val trees = mutableListOf<Position>()

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        row2hint = IntArray(rows)
        col2hint = IntArray(cols)
        for (r in 0 until rows + 1) {
            val str = layout[r]
            for (c in 0 until cols + 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == 'T')
                    trees.add(p)
                else if ((r == rows) != (c == cols)) {
                    val n = if (ch == ' ') PUZ_UNKNOWN else ch - '0'
                    if (r == rows)
                        col2hint[c] = n
                    else if (c == cols)
                        row2hint[r] = n
                }
            }
        }
        val state = TentsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun row2state(row: Int) = currentState.row2state[row]
    fun col2state(col: Int) = currentState.col2state[col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}