package com.zwstudio.logicpuzzlesandroid.puzzles.snakemaze

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SnakeMazeGame(layout: List<String>, gi: GameInterface<SnakeMazeGame, SnakeMazeGameMove, SnakeMazeGameState>, gdi: GameDocumentInterface) : CellsGame<SnakeMazeGame, SnakeMazeGameMove, SnakeMazeGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val chars = "^>v<"
        const val PUZ_DIR_SQUARE = -1
    }

    var pos2hint = mutableMapOf<Position, SnakeMazeHint>()

    init {
        size = Position(layout.size, layout[0].length / 2)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val s = str.substring(c * 2, c * 2 + 2).trim()
                if (s.isEmpty()) continue
                val num = s[0] - '0'
                val dir = chars.indexOf(s[1])
                pos2hint[Position(r, c)] = SnakeMazeHint(num, dir)
            }
        }
        val state = SnakeMazeGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2StateHint(p: Position) = currentState.pos2stateHint[p]
    fun pos2StateAllowed(p: Position) = currentState.pos2stateAllowed[p]
    fun squares() = currentState.squares
}
