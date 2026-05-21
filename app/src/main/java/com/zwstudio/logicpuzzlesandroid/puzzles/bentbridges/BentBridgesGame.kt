package com.zwstudio.logicpuzzlesandroid.puzzles.bentbridges

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BentBridgesGame(layout: List<String>, gi: GameInterface<BentBridgesGame, BentBridgesGameMove, BentBridgesGameState>, gdi: GameDocumentInterface) : CellsGame<BentBridgesGame, BentBridgesGameMove, BentBridgesGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0..<rows) {
            val str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch.isDigit())
                    pos2hint[p] = ch - '0'
            }
        }
        val state = BentBridgesGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}