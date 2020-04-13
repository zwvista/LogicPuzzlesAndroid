package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CarpentersSquareGame(layout: List<String>, gi: GameInterface<CarpentersSquareGame, CarpentersSquareGameMove, CarpentersSquareGameState>, gdi: GameDocumentInterface) : CellsGame<CarpentersSquareGame, CarpentersSquareGameMove, CarpentersSquareGameState>(gi, gdi) {
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
    var pos2hint = mutableMapOf<Position, CarpenterSquareHint>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = get(p.row, p.col)

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        objArray = Array(rows() * cols()) { Array(4) {GridLineObject.Empty} }
        for (r in 0 until rows() - 1) {
            val str = layout[r]
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                when (val ch = str[c]) {
                    in '0'..'9' -> pos2hint[p] = CarpentersSquareCornerHint(ch - '0')
                    'O' -> pos2hint[p] = CarpentersSquareCornerHint()
                    '^' -> pos2hint[p] = CarpentersSquareUpHint()
                    'v' -> pos2hint[p] = CarpentersSquareDownHint()
                    '<' -> pos2hint[p] = CarpentersSquareLeftHint()
                    '>' -> pos2hint[p] = CarpentersSquareRightHint()
                    else -> {}
                }
            }
        }
        for (r in 0 until rows() - 1) {
            this[r, 0][2] = GridLineObject.Line
            this[r + 1, 0][0] = GridLineObject.Line
            this[r, cols() - 1][2] = GridLineObject.Line
            this[r + 1, cols() - 1][0] = GridLineObject.Line
        }
        for (c in 0 until cols() - 1) {
            this[0, c][1] = GridLineObject.Line
            this[0, c + 1][3] = GridLineObject.Line
            this[rows() - 1, c][1] = GridLineObject.Line
            this[rows() - 1, c + 1][3] = GridLineObject.Line
        }
        val state = CarpentersSquareGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: CarpentersSquareGameMove, f: (CarpentersSquareGameState, CarpentersSquareGameMove) -> Boolean): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(state())
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

    fun switchObject(move: CarpentersSquareGameMove) = changeObject(move, CarpentersSquareGameState::switchObject)
    fun setObject(move: CarpentersSquareGameMove) = changeObject(move, CarpentersSquareGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun pos2State(p: Position) = state().pos2state[p]
}