package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FutoshikiGame(layout: List<String>, gi: GameInterface<FutoshikiGame, FutoshikiGameMove, FutoshikiGameState>, gdi: GameDocumentInterface) : CellsGame<FutoshikiGame, FutoshikiGameMove, FutoshikiGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val objArray: CharArray
    val pos2hint = mutableMapOf<Position, Char>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = CharArray(rows * cols)
        for (r in 0..<rows) {
            val str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val ch = str[c]
                this[p] = ch
                if ((r % 2 != 0 || c % 2 != 0) && ch != ' ') pos2hint[p] = ch
            }
        }
        val state = FutoshikiGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun row2state(row: Int) = currentState.row2state[row]
    fun col2state(col: Int) = currentState.col2state[col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
