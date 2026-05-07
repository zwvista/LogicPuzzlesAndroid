package com.zwstudio.logicpuzzlesandroid.puzzles.crossroadblocks

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CrossroadBlocksGame(layout: List<String>, gi: GameInterface<CrossroadBlocksGame, CrossroadBlocksGameMove, CrossroadBlocksGameState>, gdi: GameDocumentInterface) : CellsGame<CrossroadBlocksGame, CrossroadBlocksGameMove, CrossroadBlocksGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val chars = "^>v<"
        const val PUZ_UNKNOWN = -1
    }

    val pos2hint = mutableMapOf<Position, CrossroadBlocksHint>()

    init {
        size = Position(layout.size, layout[0].length / 3)
        for (r in 0..<rows) {
            var str = layout[r]
            for (c in 0..<cols) {
                val s = str.substring(c * 3, c * 3 + 3)
                if (s[0] == ' ') continue
                val isBlack = s[0] == 'B'
                val num = if (s[1] == ' ') PUZ_UNKNOWN else s[1] - '0'
                val dir = if (s[2] == ' ') PUZ_UNKNOWN else chars.indexOf(s[2])
                pos2hint[Position(r, c)] = CrossroadBlocksHint(isBlack, num, dir)
            }
        }
        val state = CrossroadBlocksGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}
