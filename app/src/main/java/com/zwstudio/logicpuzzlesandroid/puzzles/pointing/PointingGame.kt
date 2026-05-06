package com.zwstudio.logicpuzzlesandroid.puzzles.pointing

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PointingGame(layout: List<String>, gi: GameInterface<PointingGame, PointingGameMove, PointingGameState>, gdi: GameDocumentInterface) : CellsGame<PointingGame, PointingGameMove, PointingGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions8
    }

    val objArray: IntArray
    val arrow2rng = mutableMapOf<Position, List<Position>>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = IntArray(rows * cols)

        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val n = str[c] - '0'
                val p = Position(r, c)
                this[p] = n
                val os = offset[n]
                var p2 = p + os
                val rng = mutableListOf<Position>()
                while (isValid(p2)) {
                    rng.add(p2)
                    p2 += os
                }
                arrow2rng[p] = rng
            }
        }

        val state = PointingGameState(this)
        levelInitialized(state)
    }

    fun isMarkedArrows(p: Position) = currentState.markedArrows.contains(p)
    fun isNonPointingArrows(p: Position) = currentState.nonPointingArrows.contains(p)
}
