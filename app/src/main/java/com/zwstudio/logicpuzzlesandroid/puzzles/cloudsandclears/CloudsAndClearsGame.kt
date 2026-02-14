package com.zwstudio.logicpuzzlesandroid.puzzles.cloudsandclears

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CloudsAndClearsGame(layout: List<String>, gi: GameInterface<CloudsAndClearsGame, CloudsAndClearsGameMove, CloudsAndClearsGameState>, gdi: GameDocumentInterface) : CellsGame<CloudsAndClearsGame, CloudsAndClearsGameMove, CloudsAndClearsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(-1, -1),
            Position(-1, 0),
            Position(0, 0),
            Position(0, -1)
        )
        val car_offset = listOf(
            listOf(Position(0, 0), Position(0, 1)),
            listOf(Position(0, 0), Position(0, 1), Position(0, 2)),
            listOf(Position(0, 0), Position(1, 0)),
            listOf(Position(0, 0), Position(1, 0), Position(2, 0)),
        )
        val car_objects = listOf(
            listOf(CloudsAndClearsObject.Left, CloudsAndClearsObject.Right),
            listOf(CloudsAndClearsObject.Left, CloudsAndClearsObject.Horizontal, CloudsAndClearsObject.Right),
            listOf(CloudsAndClearsObject.Top, CloudsAndClearsObject.Bottom),
            listOf(CloudsAndClearsObject.Top, CloudsAndClearsObject.Vertical, CloudsAndClearsObject.Bottom),
        )
    }

    val pos2hint = mutableMapOf<Position, Int>();

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ')
                    pos2hint[p] = ch - '0'
            }
        }
        val state = CloudsAndClearsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position): CloudsAndClearsObject = currentState[p]
    fun getObject(row: Int, col: Int): CloudsAndClearsObject = currentState[row, col]
    fun getStateHint(p: Position) = currentState.pos2stateHint[p]
    fun getStateAllowed(p: Position) = currentState.pos2stateAllowed[p]
}
