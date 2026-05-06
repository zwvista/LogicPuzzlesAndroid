package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MathraxGame(layout: List<String>, gi: GameInterface<MathraxGame, MathraxGameMove, MathraxGameState>, gdi: GameDocumentInterface) : CellsGame<MathraxGame, MathraxGameMove, MathraxGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 0),
            Position(0, 1)
        )
    }

    val objArray: IntArray
    val pos2hint = hashMapOf<Position, MathraxHint>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size / 2 + 1, layout[0].length)
        objArray = IntArray(rows * cols)
        var i = 0
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val ch = str[c]
                objArray[i++] = if (ch == ' ') 0 else ch - '0'
            }
        }
        for (r in 0 until rows - 1) {
            val str = layout[rows + r]
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val s = str.substring(c * 3, c * 3 + 2)
                val ch = str[c * 3 + 2]
                if (ch == ' ') continue
                pos2hint[p] = MathraxHint(ch, if (s == "  ") 0 else s.trim(' ').toInt())
            }
        }
        val state = MathraxGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun row2state(row: Int) = currentState.row2state[row]
    fun col2state(col: Int) = currentState.col2state[col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
