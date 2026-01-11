package com.zwstudio.logicpuzzlesandroid.puzzles.culturedbranches

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CulturedBranchesGame(layout: List<String>, gi: GameInterface<CulturedBranchesGame, CulturedBranchesGameMove, CulturedBranchesGameState>, gdi: GameDocumentInterface) : CellsGame<CulturedBranchesGame, CulturedBranchesGameMove, CulturedBranchesGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
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
        val state = CulturedBranchesGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}