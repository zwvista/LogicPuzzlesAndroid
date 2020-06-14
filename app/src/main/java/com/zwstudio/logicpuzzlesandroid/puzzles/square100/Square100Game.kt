package com.zwstudio.logicpuzzlesandroid.puzzles.square100

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class Square100Game(layout: List<String>, gi: GameInterface<Square100Game, Square100GameMove, Square100GameState>, gdi: GameDocumentInterface) : CellsGame<Square100Game, Square100GameMove, Square100GameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var objArray: Array<String>
    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: String) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: String) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { "" }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val s = " " + str[c] + " "
                this[r, c] = s
            }
        }
        val state = Square100GameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getRowHint(row: Int) = currentState.row2hint[row]
    fun getColHint(col: Int) = currentState.col2hint[col]
}