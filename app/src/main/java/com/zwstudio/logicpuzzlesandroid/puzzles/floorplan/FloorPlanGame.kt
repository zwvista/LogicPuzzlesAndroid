package com.zwstudio.logicpuzzlesandroid.puzzles.floorplan

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FloorPlanGame(layout: List<String>, gi: GameInterface<FloorPlanGame, FloorPlanGameMove, FloorPlanGameState>, gdi: GameDocumentInterface) : CellsGame<FloorPlanGame, FloorPlanGameMove, FloorPlanGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val PUZ_EMPTY = 0
        const val PUZ_MARKER = -1
        const val PUZ_FORBIDDEN = -2
    }

    var objArray: IntArray

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = IntArray(rows * cols)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val ch = str[c]
                this[r, c] = if (ch == ' ') PUZ_EMPTY else ch - '0'
            }
        }
        val state = FloorPlanGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}
