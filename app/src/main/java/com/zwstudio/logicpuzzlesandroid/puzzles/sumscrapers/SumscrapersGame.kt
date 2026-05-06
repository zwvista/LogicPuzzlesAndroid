package com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SumscrapersGame(layout: List<String>, gi: GameInterface<SumscrapersGame, SumscrapersGameMove, SumscrapersGameState>, gdi: GameDocumentInterface) : CellsGame<SumscrapersGame, SumscrapersGameMove, SumscrapersGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val objArray: IntArray
    fun intMax() = rows - 2

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}
    override fun isValid(row: Int, col: Int) = row in 1..<size.row - 1 && col in 1..<size.col - 1

    init {
        size = Position(layout.size, layout[0].length / 2)
        objArray = IntArray(rows * cols)
        for (r in 0..<rows) {
            var str = layout[r]
            for (c in 0..<cols) {
                val s = str.substring(c * 2, c * 2 + 2).trim(' ')
                val n = if (s == "") 0 else s.toInt()
                this[r, c] = n
            }
        }
        val state = SumscrapersGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(row: Int, col: Int) = currentState.pos2state(row, col)
}
