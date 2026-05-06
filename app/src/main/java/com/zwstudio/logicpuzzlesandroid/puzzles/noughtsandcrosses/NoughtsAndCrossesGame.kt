package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NoughtsAndCrossesGame(layout: List<String>, var chMax: Char, gi: GameInterface<NoughtsAndCrossesGame, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState>, gdi: GameDocumentInterface) : CellsGame<NoughtsAndCrossesGame, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        val dirs = intArrayOf(1, 0, 3, 2)
    }

    val objArray: CharArray

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    val noughts = mutableListOf<Position>()

    init {
        size = Position(layout.size, layout[0].length)
        objArray = CharArray(rows * cols)
        for (r in 0 until rows) {
            var str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == 'O') noughts.add(p)
                this[p] = if (ch == 'O') ' ' else ch
            }
        }
        val state = NoughtsAndCrossesGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun row2state(row: Int) = currentState.row2state[row]
    fun col2state(col: Int) = currentState.col2state[col]
    fun pos2state(p: Position) = currentState.pos2state[p] ?: HintState.Normal
}
