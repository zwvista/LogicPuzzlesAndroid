package com.zwstudio.logicpuzzlesandroid.puzzles.archipelago

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.NorthEast
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.NorthWest
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.SouthEast
import com.zwstudio.logicpuzzlesandroid.common.domain.Position.Companion.SouthWest

class ArchipelagoGame(layout: List<String>, gi: GameInterface<ArchipelagoGame, ArchipelagoGameMove, ArchipelagoGameState>, gdi: GameDocumentInterface) : CellsGame<ArchipelagoGame, ArchipelagoGameMove, ArchipelagoGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Square2x2Offset
        val offset3 = listOf(
            NorthEast,
            SouthEast,
            SouthWest,
            NorthWest,
        )
    }

    val pos2hint = mutableMapOf<Position, Int>()

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
        val state = ArchipelagoGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun invalid2x2Squares() = currentState.invalid2x2Squares
}
