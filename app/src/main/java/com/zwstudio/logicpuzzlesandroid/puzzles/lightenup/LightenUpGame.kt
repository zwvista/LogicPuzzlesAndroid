package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LightenUpGame(layout: List<String>, gi: GameInterface<LightenUpGame, LightenUpGameMove, LightenUpGameState>, gdi: GameDocumentInterface) : CellsGame<LightenUpGame, LightenUpGameMove, LightenUpGameState>(gi, gdi) {
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
                if (ch == 'W')
                    pos2hint[p] = -1
                else if (ch.isDigit())
                    pos2hint[p] = ch - '0'
            }
        }
        val state = LightenUpGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2StateHint(p: Position) = currentState.pos2stateHint[p]
    fun pos2StateAllowed(p: Position) = currentState.pos2stateAllowed[p]
}
