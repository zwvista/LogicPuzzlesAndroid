package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TapDifferentlyGame(layout: List<String>, gi: GameInterface<TapDifferentlyGame, TapDifferentlyGameMove, TapDifferentlyGameState>, gdi: GameDocumentInterface) : CellsGame<TapDifferentlyGame, TapDifferentlyGameMove, TapDifferentlyGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions8
        val offset2 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(1, 1)
        )
        val offset3 = Position.Directions4
    }

    val pos2hint = mutableMapOf<Position, List<Int>>()

    init {
        size = Position(layout.size, layout[0].length / 4)
        for (r in 0..<rows) {
            var str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val s = str.substring(c * 4, c * 4 + 4).trim(' ')
                if (s.isEmpty()) continue
                val hint = mutableListOf<Int>()
                for (ch in s.toCharArray()) {
                    if (ch == '?' || ch in '0'..'9') {
                        val n = if (ch == '?') -1 else ch - '0'
                        hint.add(n)
                    }
                }
                pos2hint[p] = hint
            }
        }
        val state = TapDifferentlyGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
