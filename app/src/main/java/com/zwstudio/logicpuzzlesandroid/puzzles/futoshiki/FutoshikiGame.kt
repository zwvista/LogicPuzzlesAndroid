package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FutoshikiGame(layout: List<String>, gi: GameInterface<FutoshikiGame, FutoshikiGameMove, FutoshikiGameState>, gdi: GameDocumentInterface) : CellsGame<FutoshikiGame, FutoshikiGameMove, FutoshikiGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var objArray: CharArray
    var pos2hint = mutableMapOf<Position, Char>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = CharArray(rows * cols)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                this[p] = ch
                if ((r % 2 != 0 || c % 2 != 0) && ch != ' ') pos2hint[p] = ch
            }
        }
        val state = FutoshikiGameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getRowState(row: Int) = currentState.row2state[row]
    fun getColState(col: Int) = currentState.col2state[col]
    fun getPosState(p: Position) = currentState.pos2state[p]
}
