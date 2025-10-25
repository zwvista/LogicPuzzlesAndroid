package com.zwstudio.logicpuzzlesandroid.puzzles.desertdunes

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DesertDunesGame(layout: List<String>, gi: GameInterface<DesertDunesGame, DesertDunesGameMove, DesertDunesGameState>, gdi: GameDocumentInterface) : CellsGame<DesertDunesGame, DesertDunesGameMove, DesertDunesGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Square2x2Offset
    }

    var pos2hint = mutableMapOf<Position, Int>()

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
        val state = DesertDunesGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun invalid2x2Squares() = currentState.invalid2x2Squares
}