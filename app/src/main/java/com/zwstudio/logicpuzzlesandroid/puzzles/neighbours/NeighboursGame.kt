package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NeighboursGame(layout: List<String>, gi: GameInterface<NeighboursGame, NeighboursGameMove, NeighboursGameState>, gdi: GameDocumentInterface) : CellsGame<NeighboursGame, NeighboursGameMove, NeighboursGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        var offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        var dirs = intArrayOf(1, 0, 3, 2)
    }

    var objArray: Array<Array<GridLineObject>>
    var pos2hint = mutableMapOf<Position, Int>()
    var areaSize = 0

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<GridLineObject>) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        objArray = Array(rows * cols) { Array(4) { GridLineObject.Empty } }
        for (r in 0 until rows - 1) {
            val str = layout[r]
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == ' ') continue
                val n = ch - '0'
                pos2hint[p] = n
            }
        }
        areaSize = (rows - 1) * (cols - 1) / pos2hint.size
        for (r in 0 until rows - 1) {
            this[r, 0][2] = GridLineObject.Line
            this[r + 1, 0][0] = GridLineObject.Line
            this[r, cols - 1][2] = GridLineObject.Line
            this[r + 1, cols - 1][0] = GridLineObject.Line
        }
        for (c in 0 until cols - 1) {
            this[0, c][1] = GridLineObject.Line
            this[0, c + 1][3] = GridLineObject.Line
            this[rows - 1, c][1] = GridLineObject.Line
            this[rows - 1, c + 1][3] = GridLineObject.Line
        }
        val state = NeighboursGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    fun switchObject(move: NeighboursGameMove) = changeObject(move, NeighboursGameState::switchObject)
    fun setObject(move: NeighboursGameMove) = changeObject(move, NeighboursGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}
