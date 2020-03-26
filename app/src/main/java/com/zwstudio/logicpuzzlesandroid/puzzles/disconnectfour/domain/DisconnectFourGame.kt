package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import fj.F2

class DisconnectFourGame(layout: List<String>, gi: GameInterface<DisconnectFourGame?, DisconnectFourGameMove?, DisconnectFourGameState?>?, gdi: GameDocumentInterface?) : CellsGame<DisconnectFourGame?, DisconnectFourGameMove?, DisconnectFourGameState?>(gi, gdi) {
    var objArray: Array<DisconnectFourObject?>
    operator fun get(row: Int, col: Int): DisconnectFourObject? {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position?): DisconnectFourObject? {
        return get(p!!.row, p.col)
    }

    operator fun set(row: Int, col: Int, dotObj: DisconnectFourObject?) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: DisconnectFourObject?) {
        set(p.row, p.col, obj)
    }

    private fun changeObject(move: DisconnectFourGameMove?, f: F2<DisconnectFourGameState?, DisconnectFourGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: DisconnectFourGameMove?): Boolean {
        return changeObject(move, F2 { obj: DisconnectFourGameState?, move: DisconnectFourGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: DisconnectFourGameMove?): Boolean {
        return changeObject(move, F2 { obj: DisconnectFourGameState?, move: DisconnectFourGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): DisconnectFourObject? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): DisconnectFourObject? {
        return state()!![row, col]
    }

    fun pos2State(p: Position?): AllowedObjectState? {
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
                objArray[i++] = if (ch == 'Y') DisconnectFourObject.Yellow else if (ch == 'R') DisconnectFourObject.Red else DisconnectFourObject.Empty
            }
            r++
        }
        val state = DisconnectFourGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}