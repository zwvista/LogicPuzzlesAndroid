package com.zwstudio.logicpuzzlesandroid.puzzles.guesstris

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class GuesstrisGame(layout: List<String>, gi: GameInterface<GuesstrisGame, GuesstrisGameMove, GuesstrisGameState>, gdi: GameDocumentInterface) : CellsGame<GuesstrisGame, GuesstrisGameMove, GuesstrisGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        var dirs = intArrayOf(1, 0, 3, 2)
        val offset3 = Position.Square2x2Offset
        const val PUZ_SQUARE = 'S'
        const val PUZ_TRIANGLE = 'T'
        const val PUZ_CIRCLE = 'C'
        const val PUZ_DIAMOND = 'D'
        var tetrominoes = arrayOf(
            arrayOf(
                arrayOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(2, 1)),
                arrayOf(Position(0, 1), Position(1, 1), Position(2, 0), Position(2, 1)),
                arrayOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(1, 0)),
                arrayOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(1, 2)),
                arrayOf(Position(0, 0), Position(0, 1), Position(1, 0), Position(2, 0)),
                arrayOf(Position(0, 0), Position(0, 1), Position(1, 1), Position(2, 1)),
                arrayOf(Position(0, 0), Position(1, 0), Position(1, 1), Position(1, 2)),
                arrayOf(Position(0, 2), Position(1, 0), Position(1, 1), Position(1, 2))
            ),
            arrayOf(
                arrayOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0)),
                arrayOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(0, 3))
            ),
            arrayOf(
                arrayOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(1, 1)),
                arrayOf(Position(0, 1), Position(1, 0), Position(1, 1), Position(2, 1)),
                arrayOf(Position(0, 1), Position(1, 0), Position(1, 1), Position(1, 2)),
                arrayOf(Position(0, 0), Position(1, 0), Position(1, 1), Position(2, 0))
            ),
            arrayOf(
                arrayOf(Position(0, 0), Position(0, 1), Position(1, 1), Position(1, 2)),
                arrayOf(Position(0, 1), Position(0, 2), Position(1, 0), Position(1, 1)),
                arrayOf(Position(0, 0), Position(1, 0), Position(1, 1), Position(2, 1)),
                arrayOf(Position(0, 1), Position(1, 0), Position(1, 1), Position(2, 0))
            )
        )
    }

    var objArray: MutableList<MutableList<GridLineObject>>
    val pos2char = mutableMapOf<Position, Char>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        objArray = MutableList(rows * cols) { MutableList(4) { GridLineObject.Empty } }
        for (r in 0 until rows - 1) {
            val str = layout[r]
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val ch = str[c]
                pos2char[p] = ch
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
        val state = GuesstrisGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}