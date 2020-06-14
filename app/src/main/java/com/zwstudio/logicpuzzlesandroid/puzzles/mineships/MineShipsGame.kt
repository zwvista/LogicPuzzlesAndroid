package com.zwstudio.logicpuzzlesandroid.puzzles.mineships

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MineShipsGame(layout: List<String>, gi: GameInterface<MineShipsGame, MineShipsGameMove, MineShipsGameState>, gdi: GameDocumentInterface) : CellsGame<MineShipsGame, MineShipsGameMove, MineShipsGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1)
        )
    }

    var pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val ch = str[c]
                if (ch in '0'..'9') {
                    val n = ch - '0'
                    pos2hint[Position(r, c)] = n
                }
            }
        }
        val state = MineShipsGameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}
