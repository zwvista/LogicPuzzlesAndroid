package com.zwstudio.logicpuzzlesandroid.puzzles.inbetweennurikabe

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class InbetweenNurikabeGame(layout: List<String>, gi: GameInterface<InbetweenNurikabeGame, InbetweenNurikabeGameMove, InbetweenNurikabeGameState>, gdi: GameDocumentInterface) : CellsGame<InbetweenNurikabeGame, InbetweenNurikabeGameMove, InbetweenNurikabeGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Square2x2Offset
    }

    val pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            var str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch in '0'..'9') {
                    val n = ch - '0'
                    pos2hint[p] = n
                }
            }
        }
        val state = InbetweenNurikabeGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
    fun invalid2x2Squares() = currentState.invalid2x2Squares
}
