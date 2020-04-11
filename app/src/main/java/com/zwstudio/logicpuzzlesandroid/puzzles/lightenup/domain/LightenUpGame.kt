package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import fj.F2
import java.util.*

class LightenUpGame(layout: List<String>, gi: GameInterface<LightenUpGame?, LightenUpGameMove?, LightenUpGameState?>?, gdi: GameDocumentInterface?) : CellsGame<LightenUpGame?, LightenUpGameMove?, LightenUpGameState?>(gi, gdi) {
    var pos2hint: MutableMap<Position?, Int> = HashMap()
    private fun changeObject(move: LightenUpGameMove?, f: F2<LightenUpGameState?, LightenUpGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: LightenUpGameMove?): Boolean {
        return changeObject(move, F2 { obj: LightenUpGameState?, move: LightenUpGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: LightenUpGameMove?): Boolean {
        return changeObject(move, F2 { obj: LightenUpGameState?, move: LightenUpGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): LightenUpObject? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): LightenUpObject? {
        return state()!![row, col]
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
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == 'W' || ch >= '0' && ch <= '9') {
                    val n = if (ch == 'W') -1 else ch - '0'
                    pos2hint[p] = n
                }
            }
        }
        val state = LightenUpGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}