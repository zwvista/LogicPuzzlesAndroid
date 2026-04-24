package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NumberLinkGame(layout: List<String>, gi: GameInterface<NumberLinkGame, NumberLinkGameMove, NumberLinkGameState>, gdi: GameDocumentInterface) : CellsGame<NumberLinkGame, NumberLinkGameMove, NumberLinkGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val pos2hint = mutableMapOf<Position, Int>()
    var pos2rng = mutableMapOf<Int, MutableList<Position>>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == ' ') continue
                val n = if (ch.isDigit()) ch - '0' else ch - 'A' + 10
                pos2hint[p] = n
                val rng = pos2rng[n] ?: mutableListOf()
                rng.add(p)
                pos2rng[n] = rng
            }
        }
        val state = NumberLinkGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
