package com.zwstudio.logicpuzzlesandroid.puzzles.banquet

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BanquetGame(layout: List<String>, gi: GameInterface<BanquetGame, BanquetGameMove, BanquetGameState>, gdi: GameDocumentInterface) : CellsGame<BanquetGame, BanquetGameMove, BanquetGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_CANCEL_MOVE = -1
    }

    val pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0..<rows) {
            val str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ')
                    pos2hint[p] = ch - '0'
            }
        }
        val state = BanquetGameState(this)
        levelInitialized(state)
    }

    fun hint2blanket(p: Position) = currentState.hint2blanket[p]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
