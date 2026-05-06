package com.zwstudio.logicpuzzlesandroid.puzzles.magnets

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MagnetsGame(layout: List<String>, gi: GameInterface<MagnetsGame, MagnetsGameMove, MagnetsGameState>, gdi: GameDocumentInterface) : CellsGame<MagnetsGame, MagnetsGameMove, MagnetsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_UNKNOWN = -1
    }

    val row2hint: IntArray
    val col2hint: IntArray
    val areas = mutableListOf<MagnetsArea>()
    val singles = mutableListOf<Position>()

    init {
        size = Position(layout.size - 2, layout[0].length - 2)
        row2hint = IntArray(rows * 2) { PUZ_UNKNOWN }
        col2hint = IntArray(cols * 2) { PUZ_UNKNOWN }
        for (r in 0 until rows + 2) {
            val str = layout[r]
            for (c in 0 until cols + 2) {
                val p2 = Position(r, c)
                when (val ch = str[c]) {
                    '.' -> {
                        areas.add(MagnetsArea(p2))
                        singles.add(p2)
                    }
                    'H' -> areas.add(MagnetsArea(p2, MagnetsAreaType.Horizontal))
                    'V' -> areas.add(MagnetsArea(p2, MagnetsAreaType.Vertical))
                    in '0'..'9' -> {
                        val n = ch - '0'
                        if (r >= rows)
                            col2hint[c * 2 + r - rows] = n
                        else if (c >= cols)
                            row2hint[r * 2 + c - cols] = n
                    }
                }
            }
        }
        val state = MagnetsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun row2state(id: Int) = currentState.row2state[id]
    fun col2state(id: Int) = currentState.col2state[id]
}
