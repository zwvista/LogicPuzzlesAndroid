package com.zwstudio.logicpuzzlesandroid.puzzles.picnic

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PicnicGame(layout: List<String>, gi: GameInterface<PicnicGame, PicnicGameMove, PicnicGameState>, gdi: GameDocumentInterface) : CellsGame<PicnicGame, PicnicGameMove, PicnicGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Square2x2Offset
        const val PUZ_UNKWOWN = -1
    }

    val pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0..<rows) {
            val str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ') {
                    pos2hint[p] = if (ch == '?') PUZ_UNKWOWN else ch - '0'
                }
            }
        }
        val state = PicnicGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
    fun invalid2x2Squares() = currentState.invalid2x2Squares
}
