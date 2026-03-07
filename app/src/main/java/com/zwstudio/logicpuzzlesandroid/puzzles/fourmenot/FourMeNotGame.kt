package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FourMeNotGame(layout: List<String>, gi: GameInterface<FourMeNotGame, FourMeNotGameMove, FourMeNotGameState>, gdi: GameDocumentInterface) : CellsGame<FourMeNotGame, FourMeNotGameMove, FourMeNotGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    var objArray: Array<FourMeNotObject>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: FourMeNotObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: FourMeNotObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { FourMeNotObject.Empty }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols)
                when (str[c]) {
                    'F' -> this[r, c] = FourMeNotObject.Flower
                    'B' -> this[r, c] = FourMeNotObject.Block
                }
        }
        val state = FourMeNotGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
