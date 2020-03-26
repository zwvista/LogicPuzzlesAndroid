package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2

class FourMeNotGame(layout: List<String>, gi: GameInterface<FourMeNotGame?, FourMeNotGameMove?, FourMeNotGameState?>?, gdi: GameDocumentInterface?) : CellsGame<FourMeNotGame?, FourMeNotGameMove?, FourMeNotGameState?>(gi, gdi) {
    var objArray: Array<FourMeNotObject?>
    operator fun get(row: Int, col: Int): FourMeNotObject? {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position?): FourMeNotObject? {
        return get(p!!.row, p.col)
    }

    operator fun set(row: Int, col: Int, dotObj: FourMeNotObject?) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: FourMeNotObject?) {
        set(p.row, p.col, obj)
    }

    private fun changeObject(move: FourMeNotGameMove?, f: F2<FourMeNotGameState?, FourMeNotGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: FourMeNotGameMove?): Boolean {
        return changeObject(move, F2 { obj: FourMeNotGameState?, move: FourMeNotGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: FourMeNotGameMove?): Boolean {
        return changeObject(move, F2 { obj: FourMeNotGameState?, move: FourMeNotGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): FourMeNotObject? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): FourMeNotObject? {
        return state()!![row, col]
    }

    fun pos2State(p: Position?): HintState? {
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
        objArray = arrayOfNulls(rows() * cols())
        var r = 0
        var i = 0
        while (r < rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = str[c]
                objArray[i++] = if (ch == 'F') FourMeNotTreeObject() else if (ch == 'B') FourMeNotTreeObject() else FourMeNotEmptyObject()
            }
            r++
        }
        val state = FourMeNotGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}