package com.zwstudio.logicpuzzlesandroid.puzzles.banquet

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BanquetGame(layout: List<String>, gi: GameInterface<BanquetGame, BanquetGameMove, BanquetGameState>, gdi: GameDocumentInterface) : CellsGame<BanquetGame, BanquetGameMove, BanquetGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_UNKNOWN = -1
        const val PUZ_CANCEL_MOVE = -1
    }

    val pos2hint = mutableMapOf<Position, Int>()
    val fixedTables = mutableSetOf<Position>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0..<rows) {
            val str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == ' ') continue
                val n = if (ch == '?') PUZ_UNKNOWN else ch - '0'
                if (n == 0)
                    fixedTables.add(p)
                else
                    pos2hint[p] = n
            }
        }
        val state = BanquetGameState(this)
        levelInitialized(state)
    }

    fun hint2table(p: Position) = currentState.hint2table[p]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
