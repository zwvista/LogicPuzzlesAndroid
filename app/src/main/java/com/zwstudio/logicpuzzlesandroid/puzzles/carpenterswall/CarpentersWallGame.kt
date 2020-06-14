package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CarpentersWallGame(layout: List<String>, gi: GameInterface<CarpentersWallGame, CarpentersWallGameMove, CarpentersWallGameState>, gdi: GameDocumentInterface) : CellsGame<CarpentersWallGame, CarpentersWallGameMove, CarpentersWallGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        var offset2 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(1, 1)
        )
    }

    var objArray: Array<CarpentersWallObject>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: CarpentersWallObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: CarpentersWallObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { CarpentersWallEmptyObject() }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (val ch = str[c]) {
                    in '0'..'9' -> this[p] = CarpentersWallCornerObject(ch - '0')
                    'O' -> this[p] = CarpentersWallCornerObject()
                    '^' -> this[p] = CarpentersWallUpObject()
                    'v' -> this[p] = CarpentersWallDownObject()
                    '<' -> this[p] = CarpentersWallLeftObject()
                    '>' -> this[p] = CarpentersWallRightObject()
                }
            }
        }
        val state = CarpentersWallGameState(this)
        levelInitilized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}