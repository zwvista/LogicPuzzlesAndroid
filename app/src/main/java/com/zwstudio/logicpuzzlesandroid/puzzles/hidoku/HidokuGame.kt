package com.zwstudio.logicpuzzlesandroid.puzzles.hidoku

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HidokuGame(layout: List<String>, gi: GameInterface<HidokuGame, HidokuGameMove, HidokuGameState>, gdi: GameDocumentInterface) : CellsGame<HidokuGame, HidokuGameMove, HidokuGameState>(gi, gdi) {
    companion object {
        const val PUZ_UNKNOWN = 0
        const val PUZ_FORBIDDEN = -1
        var offset = Position.Directions8
    }

    var objArray: IntArray
    var pos2range = mutableMapOf<Position, List<Position>>()
    var maxNum: Int

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length / 3)
        maxNum = rows * cols
        objArray = IntArray(maxNum)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val s = str.substring(c * 3, c * 3 + 3)
                this[p] = if (s == "   ") 0 else s.trim(' ').toInt()
            }
        }
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val range = mutableListOf<Position>()
                for (i in 0 until 8) {
                    val os = offset[i]
                    var p2 = p + os
                    while (isValid(p2)) {
                        range.add(p2)
                        p2 += os
                    }
                }
                pos2range[p] = range
            }
        val state = HidokuGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun focusPos() = currentState.focusPos!!
}
