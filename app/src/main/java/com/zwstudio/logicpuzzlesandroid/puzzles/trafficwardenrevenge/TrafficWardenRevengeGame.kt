package com.zwstudio.logicpuzzlesandroid.puzzles.trafficwardenrevenge

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TrafficWardenRevengeGame(layout: List<String>, gi: GameInterface<TrafficWardenRevengeGame, TrafficWardenRevengeGameMove, TrafficWardenRevengeGameState>, gdi: GameDocumentInterface) : CellsGame<TrafficWardenRevengeGame, TrafficWardenRevengeGameMove, TrafficWardenRevengeGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_GREEN = 'G'
        const val PUZ_RED = 'R'
        const val PUZ_UNKNOWN = -1
        const val PUZ_UNKNOWN_10 = -2
    }

    val pos2hint = mutableMapOf<Position, TrafficWardenRevengeHint>()

    init {
        size = Position(layout.size, layout[0].length / 2)
        for (r in 0..<rows) {

            var str = layout[r]
            for (c in 0..<cols) {
                val (ch1, ch2) = str[c * 2] to str[c * 2 + 1]
                if (ch1 == ' ') continue
                val n = if (ch2 == '1') PUZ_UNKNOWN_10 else if (ch2.isDigit()) ch2 - '0' else PUZ_UNKNOWN
                pos2hint[Position(r, c)] = TrafficWardenRevengeHint(ch1, n)
            }
        }
        val state = TrafficWardenRevengeGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
