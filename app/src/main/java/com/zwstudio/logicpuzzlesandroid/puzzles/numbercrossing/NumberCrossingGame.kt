package com.zwstudio.logicpuzzlesandroid.puzzles.numbercrossing

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NumberCrossingGame(layout: List<String>, gi: GameInterface<NumberCrossingGame, NumberCrossingGameMove, NumberCrossingGameState>, gdi: GameDocumentInterface) : CellsGame<NumberCrossingGame, NumberCrossingGameMove, NumberCrossingGameState>(gi, gdi) {
    companion object {
        const val PUZ_UNKNOWN = -1
        const val PUZ_FORBIDDEN = -2
        val offset = Position.Directions8
    }

    var objArray: IntArray
    fun intMax() = rows - 2

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}
    override fun isValid(row: Int, col: Int) = row in 1 until size.row - 1 && col in 1 until size.col - 1

    init {
        size = Position(layout.size, layout[0].length / 2)
        objArray = IntArray(rows * cols)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val s = str.substring(c * 2, c * 2 + 2).trim(' ')
                val n = if (s == "") PUZ_UNKNOWN else s.toInt()
                this[r, c] = n
            }
        }
        val state = NumberCrossingGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getPosState(p: Position) = currentState.pos2state[p]
}
