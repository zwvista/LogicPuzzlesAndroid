package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenclouds

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HiddenCloudsGame(layout: List<String>, gi: GameInterface<HiddenCloudsGame, HiddenCloudsGameMove, HiddenCloudsGameState>, gdi: GameDocumentInterface) : CellsGame<HiddenCloudsGame, HiddenCloudsGameMove, HiddenCloudsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = listOf(
            Position.North,
            Position.East,
            Position.South,
            Position.West,
            Position.Zero,
        )
    }

    val pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0..<rows) {
            var str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch.isDigit())
                    pos2hint[p] = ch - '0'
            }
        }
        val state = HiddenCloudsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2stateHint(p: Position) = currentState.pos2stateHint[p]
    fun pos2stateAllowed(p: Position) = currentState.pos2stateAllowed[p]
}