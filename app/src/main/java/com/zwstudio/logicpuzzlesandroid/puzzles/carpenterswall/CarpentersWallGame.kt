package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CarpentersWallGame(layout: List<String>, gi: GameInterface<CarpentersWallGame, CarpentersWallGameMove, CarpentersWallGameState>, gdi: GameDocumentInterface) : CellsGame<CarpentersWallGame, CarpentersWallGameMove, CarpentersWallGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(1, 1)
        )
    }

    val objArray: Array<CarpentersWallObject>
    val pos2hint = mutableMapOf<Position, Int>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: CarpentersWallObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: CarpentersWallObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { CarpentersWallObject.Empty }
        for (r in 0..<rows) {
            var str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == 'O' || ch.isDigit()) {
                    pos2hint[p] = if (ch == 'O') 0 else ch - '0'
                    this[p] = CarpentersWallObject.Corner
                } else
                    this[p] = when (ch) {
                        '^' -> CarpentersWallObject.Up
                        'v' -> CarpentersWallObject.Down
                        '<' -> CarpentersWallObject.Left
                        '>' -> CarpentersWallObject.Right
                        else ->CarpentersWallObject.Empty
                    }
            }
        }
        val state = CarpentersWallGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}