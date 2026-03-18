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
        size = Position(layout.size, layout[0].length / 2)
        objArray = Array(rows * cols) { FussyWaiterObject() }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val (ch1, ch2) = str[c * 2] to str[c * 2 + 1]
                this[r, c] = FussyWaiterObject(ch1, ch2)
            }
        }
        val state = FussyWaiterGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2stateFood(p: Position) = currentState.pos2stateFood[p]
    fun pos2stateDrink(p: Position) = currentState.pos2stateDrink[p]
}
