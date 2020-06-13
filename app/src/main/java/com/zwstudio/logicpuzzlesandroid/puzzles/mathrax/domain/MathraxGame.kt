package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MathraxGame(layout: List<String>, gi: GameInterface<MathraxGame, MathraxGameMove, MathraxGameState>, gdi: GameDocumentInterface) : CellsGame<MathraxGame, MathraxGameMove, MathraxGameState>(gi, gdi) {
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
            Position(1, 0),
            Position(0, 1)
        )
    }

    var objArray: IntArray
    var pos2hint = hashMapOf<Position, MathraxHint>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size / 2 + 1, layout[0].length)
        objArray = IntArray(rows() * cols())
        var i = 0
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val ch = str[c]
                objArray[i++] = if (ch == ' ') 0 else ch - '0'
            }
        }
        for (r in 0 until rows() - 1) {
            val str = layout[rows() + r]
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                val s = str.substring(c * 3, c * 3 + 2)
                val ch = str[c * 3 + 2]
                if (ch == ' ') continue
                pos2hint[p] = MathraxHint(ch, if (s == "  ") 0 else s.trim(' ').toInt())
            }
        }
        val state = MathraxGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: MathraxGameMove, f: (MathraxGameState, MathraxGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: MathraxGameMove) = changeObject(move, MathraxGameState::switchObject)
    fun setObject(move: MathraxGameMove) = changeObject(move, MathraxGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun getRowState(row: Int) = state().row2state[row]
    fun getColState(col: Int) = state().col2state[col]
    fun getPosState(p: Position) = state().pos2state[p]
}
