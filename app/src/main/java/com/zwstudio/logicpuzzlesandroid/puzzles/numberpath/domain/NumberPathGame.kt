package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath.domainimport

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2

class NumberPathGame(layout: List<String>, gi: GameInterface<NumberPathGame, NumberPathGameMove, NumberPathGameState>, gdi: GameDocumentInterface) : CellsGame<NumberPathGame, NumberPathGameMove, NumberPathGameState>(gi, gdi) {
    var objArray: IntArray
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p.row, p.col)

    operator fun set(row: Int, col: Int, obj: Int) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Int) {
        set(p.row, p.col, obj)
    }

    private fun changeObject(move: NumberPathGameMove, f: (NumberPathGameState, NumberPathGameMove) -> Boolean): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state: NumberPathGameState = cloner.deepClone(state())
        val changed = f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states.get(stateIndex - 1), state)
        }
        return changed
    }

    fun setObject(move: NumberPathGameMove) = changeObject(move, F2<NumberPathGameState, NumberPathGameMove, Boolean> { state: NumberPathGameState, move2: NumberPathGameMove -> state.setObject(move2) })

    fun getObject(p: Position?) = state().get(p)

    fun getObject(row: Int, col: Int) = state().get(row, col)

    fun pos2State(p: Position?) = state().pos2state.get(p)

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1))
    }

    init {
        size = Position(layout.size, layout[0].length / 2)
        objArray = IntArray(rows() * cols())
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val s = str.substring(c * 2, c * 2 + 2).trim { it <= ' ' }
                set(p, Integer.valueOf(s))
            }
        }
        val state = NumberPathGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}