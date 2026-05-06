package com.zwstudio.logicpuzzlesandroid.puzzles.newcarpentersquare

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NewCarpenterSquareGame(layout: List<String>, gi: GameInterface<NewCarpenterSquareGame, NewCarpenterSquareGameMove, NewCarpenterSquareGameState>, gdi: GameDocumentInterface) : CellsGame<NewCarpenterSquareGame, NewCarpenterSquareGameMove, NewCarpenterSquareGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        val dirs = intArrayOf(1, 0, 3, 2)
    }

    val objArray: Array<Array<GridLineObject>>
    val pos2hint = mutableMapOf<Position, NewCarpenterSquareHint>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        objArray = Array(rows * cols) { Array(4) {GridLineObject.Empty} }
        for (r in 0 until rows - 1) {
            var str = layout[r]
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                when (str[c]) {
                    '=' -> pos2hint[p] = NewCarpenterSquareHint.Equal
                    '/' -> pos2hint[p] = NewCarpenterSquareHint.NotEqual
                    '?' -> pos2hint[p] = NewCarpenterSquareHint.Unknown
                    else -> {}
                }
            }
        }
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
        val state = NewCarpenterSquareGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}