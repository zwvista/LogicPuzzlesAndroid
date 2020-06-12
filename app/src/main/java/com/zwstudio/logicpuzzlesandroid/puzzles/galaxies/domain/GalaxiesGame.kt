package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class GalaxiesGame(layout: List<String>, gi: GameInterface<GalaxiesGame, GalaxiesGameMove, GalaxiesGameState>, gdi: GameDocumentInterface) : CellsGame<GalaxiesGame, GalaxiesGameMove, GalaxiesGameState>(gi, gdi) {
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
    var galaxies = mutableSetOf<Position>()
    
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject>) {objArray[row * cols() + col] = dotObj}
    operator fun set(p: Position, obj: Array<GridLineObject>) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        objArray = Array(rows() * cols()) { Array(4) { GridLineObject.Empty } }
        for (r in 0 until rows() - 1) {
            val str = layout[r]
            for (c in 0 until cols() - 1) {
                when (str[c]) {
                    'o' -> galaxies.add(Position(r * 2 + 1, c * 2 + 1))
                    'v' -> {
                        galaxies.add(Position(r * 2 + 2, c * 2 + 1))
                        this[r + 1, c][1] = GridLineObject.Forbidden
                        this[r + 1, c + 1][3] = GridLineObject.Forbidden
                    }
                    '>' -> {
                        galaxies.add(Position(r * 2 + 1, c * 2 + 2))
                        this[r, c + 1][2] = GridLineObject.Forbidden
                        this[r + 1, c + 1][0] = GridLineObject.Forbidden
                    }
                    'x' -> {
                        galaxies.add(Position(r * 2 + 2, c * 2 + 2))
                        this[r, c + 1][2] = GridLineObject.Forbidden
                        this[r + 1, c + 1][0] = GridLineObject.Forbidden
                        this[r + 1, c][1] = GridLineObject.Forbidden
                        this[r + 1, c + 1][3] = GridLineObject.Forbidden
                        this[r + 1, c + 1][1] = GridLineObject.Forbidden
                        this[r + 1, c + 2][3] = GridLineObject.Forbidden
                        this[r + 1, c + 1][2] = GridLineObject.Forbidden
                        this[r + 2, c + 1][0] = GridLineObject.Forbidden
                    }
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
        val state = GalaxiesGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: GalaxiesGameMove, f: (GalaxiesGameState, GalaxiesGameMove) -> Boolean): Boolean {
        if (canRedo) {
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

    fun switchObject(move: GalaxiesGameMove) = changeObject(move, GalaxiesGameState::switchObject)
    fun setObject(move: GalaxiesGameMove) = changeObject(move, GalaxiesGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun pos2State(p: Position) = state().pos2state[p]
}
