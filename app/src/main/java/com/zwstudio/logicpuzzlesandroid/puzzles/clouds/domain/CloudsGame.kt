package com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class CloudsGame(layout: List<String>, gi: GameInterface<CloudsGame?, CloudsGameMove?, CloudsGameState?>?, gdi: GameDocumentInterface?) : CellsGame<CloudsGame?, CloudsGameMove?, CloudsGameState?>(gi, gdi) {
    var row2hint: IntArray
    var col2hint: IntArray
    var pos2cloud: MutableList<Position> = ArrayList()
    private fun changeObject(move: CloudsGameMove?, f: F2<CloudsGameState?, CloudsGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: CloudsGameMove?): Boolean {
        return changeObject(move, F2 { obj: CloudsGameState?, move: CloudsGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: CloudsGameMove?): Boolean {
        return changeObject(move, F2 { obj: CloudsGameState?, move: CloudsGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): CloudsObject? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): CloudsObject? {
        return state()!![row, col]
    }

    fun getRowState(row: Int): HintState? {
        return state()!!.row2state[row]
    }

    fun getColState(col: Int): HintState? {
        return state()!!.col2state[col]
    }

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1))
    }

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        row2hint = IntArray(rows())
        col2hint = IntArray(cols())
        for (r in 0 until rows() + 1) {
            val str = layout[r]
            for (c in 0 until cols() + 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == 'C') pos2cloud.add(p) else if (ch >= '0' && ch <= '9') {
                    val n = ch - '0'
                    if (r == rows()) col2hint[c] = n else if (c == cols()) row2hint[r] = n
                }
            }
        }
        val state = CloudsGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}