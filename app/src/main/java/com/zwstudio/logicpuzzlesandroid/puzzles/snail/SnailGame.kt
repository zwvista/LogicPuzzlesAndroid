package com.zwstudio.logicpuzzlesandroid.puzzles.snail

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import java.util.*

class SnailGame(layout: List<String>, gi: GameInterface<SnailGame, SnailGameMove, SnailGameState>, gdi: GameDocumentInterface) : CellsGame<SnailGame, SnailGameMove, SnailGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var objArray: CharArray
    var snailPathGrid: List<Position>
    var snailPathLine: List<Position>
    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = CharArray(rows * cols)
        Arrays.fill(objArray, ' ')
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val ch = str[c]
                if (ch in '0'..'9')
                    this[r, c] = ch
            }
        }
        fun snailPath(n: Int): List<Position> {
            val path = mutableListOf<Position>()
            val rng = mutableSetOf<Position>()
            for (r in 0 until n)
                for (c in 0 until n)
                    rng.add(Position(r, c))
            var p = Position(0, -1)
            var dir = 1
            while (rng.isNotEmpty()) {
                val p2 = p + offset[dir]
                if (rng.contains(p2))
                    rng.remove(p2.also { p = it })
                else {
                    dir = (dir + 1) % 4
                    p += offset[dir]
                    rng.remove(p)
                }
                path.add(+p)
            }
            return path
        }
        snailPathGrid = snailPath(rows)
        snailPathLine = snailPath(rows + 1)
        val state = SnailGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: SnailGameMove, f: (SnailGameState, SnailGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: SnailGameMove) = changeObject(move, SnailGameState::switchObject)
    fun setObject(move: SnailGameMove) = changeObject(move, SnailGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getRowState(row: Int) = currentState.row2state[row]
    fun getColState(col: Int) = currentState.col2state[col]
    fun getPositionState(row: Int, col: Int) = currentState.pos2state[Position(row, col)]
}