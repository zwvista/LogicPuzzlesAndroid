package com.zwstudio.logicpuzzlesandroid.puzzles.unreliablehints

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class UnreliableHintsGame(layout: List<String>, gi: GameInterface<UnreliableHintsGame, UnreliableHintsGameMove, UnreliableHintsGameState>, gdi: GameDocumentInterface) : CellsGame<UnreliableHintsGame, UnreliableHintsGameMove, UnreliableHintsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val chars = "^>v<"
    }

    val pos2hint = mutableMapOf<Position, UnreliableHintsHint>()

    init {
        size = Position(layout.size, layout[0].length / 2)
        for (r in 0..<rows) {
            var str = layout[r]
            for (c in 0..<cols) {
                val s = str.substring(c * 2, c * 2 + 2).trim()
                if (s.isEmpty()) continue
                val num = s[0] - '0'
                val dir = chars.indexOf(s[1])
                pos2hint[Position(r, c)] = UnreliableHintsHint(num, dir)
            }
        }
        val state = UnreliableHintsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2StateHint(p: Position) = currentState.pos2stateHint[p]
    fun pos2StateAllowed(p: Position) = currentState.pos2stateAllowed[p]
}
