package com.zwstudio.logicpuzzlesandroid.puzzles.nooks

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NooksGame(layout: List<String>, gi: GameInterface<NooksGame, NooksGameMove, NooksGameState>, gdi: GameDocumentInterface) : CellsGame<NooksGame, NooksGameMove, NooksGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Square2x2Offset
        const val PUZ_UNKWOWN = -1
    }

    val pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ') {
                    pos2hint[p] = if (ch == '?') PUZ_UNKWOWN else ch - '0'
                }
            }
        }
        val state = NooksGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
    fun invalid2x2Squares() = currentState.invalid2x2Squares
}
