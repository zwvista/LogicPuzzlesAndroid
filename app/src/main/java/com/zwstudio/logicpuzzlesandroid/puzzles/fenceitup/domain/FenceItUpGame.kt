package com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class FenceItUpGame(layout: List<String>, gi: GameInterface<FenceItUpGame?, FenceItUpGameMove?, FenceItUpGameState?>?, gdi: GameDocumentInterface?) : CellsGame<FenceItUpGame?, FenceItUpGameMove?, FenceItUpGameState?>(gi, gdi) {
    var objArray: Array<Array<GridLineObject?>>
    var pos2hint: MutableMap<Position?, Int?> = HashMap()
    operator fun get(row: Int, col: Int): Array<GridLineObject?> {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position?): Array<GridLineObject?> {
        return get(p!!.row, p.col)
    }

    private fun changeObject(move: FenceItUpGameMove?, f: F2<FenceItUpGameState?, FenceItUpGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: FenceItUpGameMove?): Boolean {
        return changeObject(move, F2 { obj: FenceItUpGameState?, move: FenceItUpGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: FenceItUpGameMove?): Boolean {
        return changeObject(move, F2 { obj: FenceItUpGameState?, move: FenceItUpGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): Array<GridLineObject?>? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): Array<GridLineObject?>? {
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
        var offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0))
        var dirs = intArrayOf(1, 0, 3, 2)
    }

    init {
        size = Position(layout.size + 1, layout[0].length / 2 + 1)
        objArray = arrayOfNulls(rows() * cols())
        for (i in objArray.indices) {
            objArray[i] = arrayOfNulls(4)
            Arrays.fill(objArray[i], GridLineObject.Empty)
        }
        for (r in 0 until rows() - 1) {
            val str = layout[r]
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                val s = str.substring(c * 2, c * 2 + 2)
                if (s == "  ") continue
                val n = s.trim { it <= ' ' }.toInt()
                pos2hint[p] = n
            }
        }
        for (r in 0 until rows() - 1) {
            get(r, 0)[2] = GridLineObject.Line
            get(r + 1, 0)[0] = GridLineObject.Line
            get(r, cols() - 1)[2] = GridLineObject.Line
            get(r + 1, cols() - 1)[0] = GridLineObject.Line
        }
        for (c in 0 until cols() - 1) {
            get(0, c)[1] = GridLineObject.Line
            get(0, c + 1)[3] = GridLineObject.Line
            get(rows() - 1, c)[1] = GridLineObject.Line
            get(rows() - 1, c + 1)[3] = GridLineObject.Line
        }
        val state = FenceItUpGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}