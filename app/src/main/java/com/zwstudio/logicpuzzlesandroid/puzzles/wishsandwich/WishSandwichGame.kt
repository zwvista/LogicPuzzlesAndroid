package com.zwstudio.logicpuzzlesandroid.puzzles.wishsandwich

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class WishSandwichGame(layout: List<String>, gi: GameInterface<WishSandwichGame, WishSandwichGameMove, WishSandwichGameState>, gdi: GameDocumentInterface) : CellsGame<WishSandwichGame, WishSandwichGameMove, WishSandwichGameState>(gi, gdi) {
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
        val state = WishSandwichGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getRowState(row: Int) = currentState.row2state[row]
    fun getColState(col: Int) = currentState.col2state[col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
