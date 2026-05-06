package com.zwstudio.logicpuzzlesandroid.puzzles.snakeomino

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SnakeominoGame(layout: List<String>, gi: GameInterface<SnakeominoGame, SnakeominoGameMove, SnakeominoGameState>, gdi: GameDocumentInterface) : CellsGame<SnakeominoGame, SnakeominoGameMove, SnakeominoGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_EMPTY = 0
        const val PUZ_END = 'O'
        const val PUZ_NOT_END = 'X'
    }

    val objArray: IntArray
    val pos2hint = mutableMapOf<Position, Char>()
    var nMax = 2

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length / 2)
        objArray = IntArray(rows * cols)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val (ch1, ch2) = str[c * 2] to str[c * 2 + 1]
                val n = if (ch1 == ' ') PUZ_EMPTY else ch1 - '0'
                this[p] = n
                if (nMax < n) nMax = n
                if (ch2 != ' ')
                    pos2hint[p] = ch2
            }
        }
        val state = SnakeominoGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
