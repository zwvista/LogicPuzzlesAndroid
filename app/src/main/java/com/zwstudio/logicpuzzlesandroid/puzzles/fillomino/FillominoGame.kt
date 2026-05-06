package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FillominoGame(layout: List<String>, gi: GameInterface<FillominoGame, FillominoGameMove, FillominoGameState>, gdi: GameDocumentInterface) : CellsGame<FillominoGame, FillominoGameMove, FillominoGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, 0)
        )
        val dirs = intArrayOf(1, 2, 1, 2)
    }

    val chMax: Char
    val objArray: CharArray

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = CharArray(rows * cols)
        chMax = ('0'.code + rows).toChar()
        for (r in 0..<rows) {
            val str = layout[r]
            for (c in 0..<cols) {
                val ch = str[c]
                this[r, c] = ch
            }
        }
        val state = FillominoGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
