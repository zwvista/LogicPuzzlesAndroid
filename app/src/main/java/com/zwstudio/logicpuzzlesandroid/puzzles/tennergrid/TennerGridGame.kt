package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TennerGridGame(layout: List<String>, gi: GameInterface<TennerGridGame, TennerGridGameMove, TennerGridGameState>, gdi: GameDocumentInterface) : CellsGame<TennerGridGame, TennerGridGameMove, TennerGridGameState>(gi, gdi) {
    companion object {
        val offset = arrayOf(
            Position(1, -1),
            Position(1, 0),
            Position(1, 1)
        )
    }

    val objArray: IntArray
    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj }
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length / 2)
        objArray = IntArray(rows * cols)
        for (r in 0..<rows) {
            val str = layout[r]
            for (c in 0..<cols) {
                val s = str.substring(c * 2, c * 2 + 2)
                val n = if (s == "  ") -1 else s.trim(' ').toInt()
                this[r, c] = n
            }
        }
        val state = TennerGridGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}