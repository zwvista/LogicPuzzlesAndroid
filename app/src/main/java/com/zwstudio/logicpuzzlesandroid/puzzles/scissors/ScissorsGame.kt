package com.zwstudio.logicpuzzlesandroid.puzzles.scissors

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ScissorsGame(layout: List<String>, gi: GameInterface<ScissorsGame, ScissorsGameMove, ScissorsGameState>, gdi: GameDocumentInterface) : CellsGame<ScissorsGame, ScissorsGameMove, ScissorsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Square2x2Offset
    }

    val pos2hint = mutableMapOf<Position, Int>();

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        for (r in 0..<rows + 1) {
            var str = layout[r]
            for (c in 0..<cols + 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ')
                    pos2hint[p] = ch - '0'
            }
        }
        val state = ScissorsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position): ScissorsObject = currentState[p]
    fun getObject(row: Int, col: Int): ScissorsObject = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
