package com.zwstudio.logicpuzzlesandroid.puzzles.numbercrosswords

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NumberCrosswordsGame(layout: List<String>, gi: GameInterface<NumberCrosswordsGame, NumberCrosswordsGameMove, NumberCrosswordsGameState>, gdi: GameDocumentInterface) : CellsGame<NumberCrosswordsGame, NumberCrosswordsGameMove, NumberCrosswordsGameState>(gi, gdi) {
    companion object {
        var offset = Position.Directions4
    }

    var objArray: IntArray
    fun intMax() = rows - 2

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}
    override fun isValid(row: Int, col: Int) = row >= 1 && col >= 1 && row < size.row - 1 && col < size.col - 1

    init {
        size = Position(layout.size, layout[0].length / 2)
        objArray = IntArray(rows * cols)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val s = str.substring(c * 2, c * 2 + 2).trim(' ')
                val n = if (s == "") 0 else s.toInt()
                this[r, c] = n
            }
        }
        val state = NumberCrosswordsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getState(row: Int, col: Int) = currentState.getState(row, col)
}
