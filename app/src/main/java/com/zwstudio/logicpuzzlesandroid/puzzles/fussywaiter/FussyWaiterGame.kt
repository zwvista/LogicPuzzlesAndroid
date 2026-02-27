package com.zwstudio.logicpuzzlesandroid.puzzles.fussywaiter

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FussyWaiterGame(layout: List<String>, gi: GameInterface<FussyWaiterGame, FussyWaiterGameMove, FussyWaiterGameState>, gdi: GameDocumentInterface) : CellsGame<FussyWaiterGame, FussyWaiterGameMove, FussyWaiterGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    var objArray: Array<FussyWaiterObject>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: FussyWaiterObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: FussyWaiterObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { FussyWaiterEmptyObject }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols)
                when (str[c]) {
                    'F' -> this[r, c] = FussyWaiterFlowerObject()
                    'B' -> this[r, c] = FussyWaiterBlockObject
                }
        }
        val state = FussyWaiterGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}
