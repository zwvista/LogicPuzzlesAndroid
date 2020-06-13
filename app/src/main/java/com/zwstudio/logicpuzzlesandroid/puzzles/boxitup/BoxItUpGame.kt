package com.zwstudio.logicpuzzlesandroid.puzzles.boxitup

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BoxItUpGame(layout: List<String>, gi: GameInterface<BoxItUpGame, BoxItUpGameMove, BoxItUpGameState>, gdi: GameDocumentInterface) : CellsGame<BoxItUpGame, BoxItUpGameMove, BoxItUpGameState>(gi, gdi) {
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

    var objArray: MutableList<MutableList<GridLineObject>>
    var pos2hint = mutableMapOf<Position, Int>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        size = Position(layout.size + 1, layout[0].length / 2 + 1)
        objArray = MutableList(rows * cols) { MutableList(4) { GridLineObject.Empty } }
        for (r in 0 until rows - 1) {
            val str = layout[r]
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val s = str.substring(c * 2, c * 2 + 2)
                if (s == "  ") continue
                val n = s.trim(' ').toInt()
                pos2hint[p] = n
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
        val state = BoxItUpGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    fun switchObject(move: BoxItUpGameMove) = changeObject(move, BoxItUpGameState::switchObject)
    fun setObject(move: BoxItUpGameMove) = changeObject(move, BoxItUpGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getState(p: Position) = currentState.pos2state[p]
}