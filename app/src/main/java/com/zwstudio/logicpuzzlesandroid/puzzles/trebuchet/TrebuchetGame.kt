package com.zwstudio.logicpuzzlesandroid.puzzles.trebuchet

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TrebuchetGame(layout: List<String>, gi: GameInterface<TrebuchetGame, TrebuchetGameMove, TrebuchetGameState>, gdi: GameDocumentInterface) : CellsGame<TrebuchetGame, TrebuchetGameMove, TrebuchetGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val pos2hint = mutableMapOf<Position, Int>()
    val pos2targets = mutableMapOf<Position, List<Position>>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ')
                    pos2hint[p] = if (Character.isDigit(ch)) ch - '0' else ch - 'A' + 10
            }
        }
        for ((p, hint) in pos2hint)
            pos2targets[p] = offset.map { os ->
                var p2 = p
                for (i in 0 until hint)
                    p2 += os
                p2
            }.filter { isValid(it) }.toList()
        val state = TrebuchetGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}