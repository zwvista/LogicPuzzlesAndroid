package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

import java.util.HashMap

import fj.F2

class ABCPathGame(layout: List<String>, gi: GameInterface<ABCPathGame, ABCPathGameMove, ABCPathGameState>, gdi: GameDocumentInterface) : CellsGame<ABCPathGame, ABCPathGameMove, ABCPathGameState>(gi, gdi) {

    companion object {
        val offset = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1)
        )
    }

    var ch2pos = mutableMapOf<Char, Position>()
    var objArray: CharArray

    override fun isValid(row: Int, col: Int) = row in 1 until size.row - 1 && col in 1 until size.col - 1
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = CharArray(rows() * cols())

        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = str[c]
                this[p] = ch
                if (r == 0 || r == rows() - 1 || c == 0 || c == cols() - 1)
                    ch2pos[ch] = p
            }
        }

        val state = ABCPathGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: ABCPathGameMove, f: (ABCPathGameState, ABCPathGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: ABCPathGameMove) = changeObject(move, ABCPathGameState::switchObject)
    fun setObject(move: ABCPathGameMove) = changeObject(move, ABCPathGameState::setObject)
    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun getState(row: Int, col: Int) = state().pos2state[Position(row, col)]
}
