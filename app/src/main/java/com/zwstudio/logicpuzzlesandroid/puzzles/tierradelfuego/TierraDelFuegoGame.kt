package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TierraDelFuegoGame(layout: List<String>, gi: GameInterface<TierraDelFuegoGame, TierraDelFuegoGameMove, TierraDelFuegoGameState>, gdi: GameDocumentInterface) : CellsGame<TierraDelFuegoGame, TierraDelFuegoGameMove, TierraDelFuegoGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var pos2hint = mutableMapOf<Position, Char>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ') pos2hint[p] = ch
            }
        }
        val state = TierraDelFuegoGameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}