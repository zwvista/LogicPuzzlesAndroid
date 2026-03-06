package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BootyIslandGame(layout: List<String>, gi: GameInterface<BootyIslandGame, BootyIslandGameMove, BootyIslandGameState>, gdi: GameDocumentInterface) : CellsGame<BootyIslandGame, BootyIslandGameMove, BootyIslandGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions8
    }

    val pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch in '0'..'9')
                    pos2hint[p] = ch - '0'
            }
        }
        val state = BootyIslandGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position): BootyIslandObject = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
