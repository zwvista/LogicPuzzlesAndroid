package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ParkLakesGame(layout: List<String>, gi: GameInterface<ParkLakesGame, ParkLakesGameMove, ParkLakesGameState>, gdi: GameDocumentInterface) : CellsGame<ParkLakesGame, ParkLakesGameMove, ParkLakesGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length / 2)
        for (r in 0..<rows) {
            val str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val s = str.substring(c * 2, c * 2 + 2)
                if (s != "  ") pos2hint[p] = if (s == " ?") -1 else s.trim(' ').toInt()
            }
        }
        val state = ParkLakesGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2stateHint(p: Position) = currentState.pos2stateHint[p]
    fun pos2stateAllowed(p: Position) = currentState.pos2stateAllowed[p]
}
