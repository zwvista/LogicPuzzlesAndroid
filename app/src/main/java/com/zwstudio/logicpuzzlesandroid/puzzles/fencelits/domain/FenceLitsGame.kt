package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FenceLitsGame(layout: List<String>, gi: GameInterface<FenceLitsGame, FenceLitsGameMove, FenceLitsGameState>, gdi: GameDocumentInterface) : CellsGame<FenceLitsGame, FenceLitsGameMove, FenceLitsGameState>(gi, gdi) {
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
        var tetrominoes = arrayOf(
            arrayOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(2, 1)),
            arrayOf(Position(0, 1), Position(1, 1), Position(2, 0), Position(2, 1)),
            arrayOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(1, 0)),
            arrayOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(1, 2)),
            arrayOf(Position(0, 0), Position(0, 1), Position(1, 0), Position(2, 0)),
            arrayOf(Position(0, 0), Position(0, 1), Position(1, 1), Position(2, 1)),
            arrayOf(Position(0, 0), Position(1, 0), Position(1, 1), Position(1, 2)),
            arrayOf(Position(0, 2), Position(1, 0), Position(1, 1), Position(1, 2)),
            arrayOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0)),
            arrayOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(0, 3)),
            arrayOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(1, 1)),
            arrayOf(Position(0, 1), Position(1, 0), Position(1, 1), Position(2, 1)),
            arrayOf(Position(0, 1), Position(1, 0), Position(1, 1), Position(1, 2)),
            arrayOf(Position(0, 0), Position(1, 0), Position(1, 1), Position(2, 0)),
            arrayOf(Position(0, 0), Position(0, 1), Position(1, 1), Position(1, 2)),
            arrayOf(Position(0, 1), Position(0, 2), Position(1, 0), Position(1, 1)),
            arrayOf(Position(0, 0), Position(1, 0), Position(1, 1), Position(2, 1)),
            arrayOf(Position(0, 1), Position(1, 0), Position(1, 1), Position(2, 0)),
            arrayOf(Position(0, 0), Position(0, 1), Position(1, 0), Position(1, 1))
        )
    }

    var objArray: Array<Array<GridLineObject>>
    var pos2hint = mutableMapOf<Position, Int>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        objArray = Array(rows * cols) { Array(4) { GridLineObject.Empty } }
        for (r in 0 until rows - 1) {
            val str = layout[r]
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch in '0'..'9')
                    pos2hint[p] = ch - '0'
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
        val state = FenceLitsGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: FenceLitsGameMove, f: (FenceLitsGameState, FenceLitsGameMove) -> Boolean): Boolean {
        if (canRedo) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(currentState)
        val changed = f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states[stateIndex - 1], state)
        }
        return changed
    }

    fun switchObject(move: FenceLitsGameMove) = changeObject(move, FenceLitsGameState::switchObject)
    fun setObject(move: FenceLitsGameMove) = changeObject(move, FenceLitsGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}
