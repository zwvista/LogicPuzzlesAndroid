package com.zwstudio.logicpuzzlesandroid.puzzles.loopandblocks

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LoopAndBlocksGame(layout: List<String>, gi: GameInterface<LoopAndBlocksGame, LoopAndBlocksGameMove, LoopAndBlocksGameState>, gdi: GameDocumentInterface) : CellsGame<LoopAndBlocksGame, LoopAndBlocksGameMove, LoopAndBlocksGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_DIR_SQUARE = -1
    }

    var pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val ch = str[c]
                if (ch != ' ')
                    pos2hint[Position(r, c)] = ch - '0'
            }
        }
        val state = LoopAndBlocksGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2StateHint(p: Position) = currentState.pos2stateHint[p]
    fun pos2StateAllowed(p: Position) = currentState.pos2stateAllowed[p]
    fun squares() = currentState.squares
}
