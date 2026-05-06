package com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CrosstownTrafficGame(layout: List<String>, gi: GameInterface<CrosstownTrafficGame, CrosstownTrafficGameMove, CrosstownTrafficGameState>, gdi: GameDocumentInterface) : CellsGame<CrosstownTrafficGame, CrosstownTrafficGameMove, CrosstownTrafficGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_UNKNOWN = -1
    }

    val pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0..<rows) {
            val str = layout[r]
            for (c in 0..<cols)
                if ((r == 0 || r == rows - 1) != (c == 0 || c == cols - 1)) {
                    val ch = str[c]
                    pos2hint[Position(r, c)] = if (ch == ' ') PUZ_UNKNOWN else ch - '0'
                }
        }
        val state = CrosstownTrafficGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
