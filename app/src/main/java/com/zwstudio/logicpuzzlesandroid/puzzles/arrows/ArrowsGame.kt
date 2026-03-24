package com.zwstudio.logicpuzzlesandroid.puzzles.arrows

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ArrowsGame(layout: List<String>, gi: GameInterface<ArrowsGame, ArrowsGameMove, ArrowsGameState>, gdi: GameDocumentInterface) : CellsGame<ArrowsGame, ArrowsGameMove, ArrowsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions8
        const val PUZ_UNKNOWN = 8
    }

    fun isCorner(p: Position): Boolean {
        val (row, col) = p
        return (row == 0 || row == rows - 1) && (col == 0 || col == cols - 1)
    }
    fun isBorder(p: Position): Boolean {
        val (row, col) = p
        return row in 1 until rows - 1 && (col == 0 || col == cols - 1) ||
                col in 1 until cols - 1 && (row == 0 || row == rows - 1)
    }

    var objArray: IntArray

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size + 2, layout[0].length + 2)
        objArray = IntArray(rows * cols) { PUZ_UNKNOWN }

        for (r in 1 until rows - 1) {
            val str = layout[r - 1]
            for (c in 1 until cols - 1) {
                val ch = str[c - 1]
                this[r, c] = ch - '0'
            }
        }

        val state = ArrowsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getHintState(p: Position) = currentState.hint2state[p]
    fun getArrowState(p: Position) = currentState.arrow2state[p]
}
