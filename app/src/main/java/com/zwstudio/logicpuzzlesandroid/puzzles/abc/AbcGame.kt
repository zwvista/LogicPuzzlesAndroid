package com.zwstudio.logicpuzzlesandroid.puzzles.abc

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class AbcGame(layout: List<String>, gi: GameInterface<AbcGame, AbcGameMove, AbcGameState>, gdi: GameDocumentInterface) : CellsGame<AbcGame, AbcGameMove, AbcGameState>(gi, gdi) {
    companion object {
        val offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    override fun isValid(row: Int, col: Int) = row in 1 until size.row - 1 && col in 1 until size.col - 1

    var objArray: CharArray
    var chMax = 'A'

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = CharArray(rows * cols) { ' ' }

        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val ch = str[c]
                this[r, c] = ch
                if (chMax < ch) chMax = ch
            }
        }

        val state = AbcGameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getState(row: Int, col: Int) = currentState.getState(row, col)
}
