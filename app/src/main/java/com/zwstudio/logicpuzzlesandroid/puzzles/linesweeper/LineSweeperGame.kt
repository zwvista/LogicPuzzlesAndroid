package com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LineSweeperGame(layout: List<String>, gi: GameInterface<LineSweeperGame, LineSweeperGameMove, LineSweeperGameState>, gdi: GameDocumentInterface) : CellsGame<LineSweeperGame, LineSweeperGameMove, LineSweeperGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions8
    }

    val pos2hint = mutableMapOf<Position, Int>()
    fun isHint(p: Position) = pos2hint.containsKey(p)

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        for (r in 0 until rows - 1) {
            var str = layout[r]
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch in '0'..'9') {
                    val n = ch - '0'
                    pos2hint[p] = n
                }
            }
        }
        val state = LineSweeperGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}