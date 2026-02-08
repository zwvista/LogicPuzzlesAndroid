package com.zwstudio.logicpuzzlesandroid.puzzles.zensolitaire

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ZenSolitaireGame(layout: List<String>, gi: GameInterface<ZenSolitaireGame, ZenSolitaireGameMove, ZenSolitaireGameState>, gdi: GameDocumentInterface) : CellsGame<ZenSolitaireGame, ZenSolitaireGameMove, ZenSolitaireGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val PUZ_STONE = -1
        val PUZ_EMPTY = 0
    }

    var stones = mutableListOf<Position>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols)
                if (str[c] != ' ')
                    stones.add(Position(r, c))
        }
        val state = ZenSolitaireGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}