package com.zwstudio.logicpuzzlesandroid.puzzles.trafficwarden

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TrafficWardenGame(layout: List<String>, gi: GameInterface<TrafficWardenGame, TrafficWardenGameMove, TrafficWardenGameState>, gdi: GameDocumentInterface) : CellsGame<TrafficWardenGame, TrafficWardenGameMove, TrafficWardenGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_GREEN = 'G'
        const val PUZ_RED = 'R'
        const val PUZ_YELLOW = 'Y'
    }

    val pos2hint = mutableMapOf<Position, TrafficWardenHint>()

    init {
        size = Position(layout.size, layout[0].length / 2)
        for (r in 0 until rows) {

            var str = layout[r]
            for (c in 0 until cols) {
                val (ch1, ch2) = str[c * 2] to str[c * 2 + 1]
                if (ch1 == ' ') continue
                val n = if (ch2.isDigit()) ch2 - '0' else ch2 - 'A' + 10
                pos2hint[Position(r, c)] = TrafficWardenHint(ch1, n)
            }
        }
        val state = TrafficWardenGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
