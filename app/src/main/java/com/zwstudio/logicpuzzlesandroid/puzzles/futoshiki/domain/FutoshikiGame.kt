package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class FutoshikiGame(layout: List<String>, gi: GameInterface<FutoshikiGame?, FutoshikiGameMove?, FutoshikiGameState?>?, gdi: GameDocumentInterface?) : CellsGame<FutoshikiGame?, FutoshikiGameMove?, FutoshikiGameState?>(gi, gdi) {
    var objArray: CharArray
    var pos2hint: MutableMap<Position, Char> = HashMap()
    operator fun get(row: Int, col: Int): Char {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position?): Char {
        return get(p!!.row, p.col)
    }

    operator fun set(row: Int, col: Int, obj: Char) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Char) {
        set(p.row, p.col, obj)
    }

    private fun changeObject(move: FutoshikiGameMove?, f: F2<FutoshikiGameState?, FutoshikiGameMove?, Boolean>): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(state())
        val changed = f.f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states[stateIndex - 1], state)
        }
        return changed
    }

    fun switchObject(move: FutoshikiGameMove?): Boolean {
        return changeObject(move, F2 { obj: FutoshikiGameState?, move: FutoshikiGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: FutoshikiGameMove?): Boolean {
        return changeObject(move, F2 { obj: FutoshikiGameState?, move: FutoshikiGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): Char {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): Char {
        return state()!![row, col]
    }

    fun getRowState(row: Int): HintState? {
        return state()!!.row2state[row]
    }

    fun getColState(col: Int): HintState? {
        return state()!!.col2state[col]
    }

    fun getPosState(p: Position?): HintState? {
        return state()!!.pos2state[p]
    }

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1))
    }

    init {
        size = Position(layout.size, layout[0].length)
        objArray = CharArray(rows() * cols())
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = str[c]
                set(p, ch)
                if ((r % 2 != 0 || c % 2 != 0) && ch != ' ') pos2hint[p] = ch
            }
        }
        val state = FutoshikiGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}