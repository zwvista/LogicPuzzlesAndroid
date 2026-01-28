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
        const val PUZ_YELLOW = 'Y'
    }

    val pos2hint = mutableMapOf<Position, TrafficWardenRevengeHint>()

    init {
        size = Position(layout.size, layout[0].length / 2)
        for (r in 0 until rows) {

            val str = layout[r]
            for (c in 0 until cols) {
                val (ch1, ch2) = str[c * 2] to str[c * 2 + 1]
                if (ch1 == ' ') continue
                val n = if (ch2.isDigit()) ch2 - '0' else ch2 - 'A' + 10
                pos2hint[Position(r, c)] = TrafficWardenRevengeHint(ch1, n)
            }
        }
        val state = TrafficWardenRevengeGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}
