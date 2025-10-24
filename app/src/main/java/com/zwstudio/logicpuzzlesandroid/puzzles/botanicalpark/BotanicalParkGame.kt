package com.zwstudio.logicpuzzlesandroid.puzzles.botanicalpark

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BotanicalParkGame(layout: List<String>, val plantsInEachArea: Int, gi: GameInterface<BotanicalParkGame, BotanicalParkGameMove, BotanicalParkGameState>, gdi: GameDocumentInterface) : CellsGame<BotanicalParkGame, BotanicalParkGameMove, BotanicalParkGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions8
    }

    var pos2arrow = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ') {
                    val n = ch - '0'
                    pos2arrow[p] = n
                }
            }
        }
        val state = BotanicalParkGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}