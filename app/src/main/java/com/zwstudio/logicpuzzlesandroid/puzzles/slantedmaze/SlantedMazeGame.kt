package com.zwstudio.logicpuzzlesandroid.puzzles.slantedmaze

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SlantedMazeGame(layout: List<String>, gi: GameInterface<SlantedMazeGame, SlantedMazeGameMove, SlantedMazeGameState>, gdi: GameDocumentInterface) : CellsGame<SlantedMazeGame, SlantedMazeGameMove, SlantedMazeGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Square2x2Offset
    }

    val pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        for (r in 0..<rows + 1) {
            var str = layout[r]
            for (c in 0..<cols + 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ')
                    pos2hint[p] = ch - '0'
            }
        }
        val state = SlantedMazeGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position): SlantedMazeObject = currentState[p]
    fun getObject(row: Int, col: Int): SlantedMazeObject = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
