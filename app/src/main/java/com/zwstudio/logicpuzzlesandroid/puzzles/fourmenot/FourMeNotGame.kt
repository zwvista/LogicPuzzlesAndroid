package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FourMeNotGame(layout: List<String>, gi: GameInterface<FourMeNotGame, FourMeNotGameMove, FourMeNotGameState>, gdi: GameDocumentInterface) : CellsGame<FourMeNotGame, FourMeNotGameMove, FourMeNotGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var objArray: Array<FourMeNotObject>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: FourMeNotObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: FourMeNotObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { FourMeNotEmptyObject }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols)
                when (str[c]) {
                    'F' -> this[r, c] = FourMeNotTreeObject()
                    'B' -> this[r, c] = FourMeNotBlockObject
                }
        }
        val state = FourMeNotGameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}
