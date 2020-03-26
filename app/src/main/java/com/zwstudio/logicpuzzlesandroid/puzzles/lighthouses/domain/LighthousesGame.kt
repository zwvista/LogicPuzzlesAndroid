package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class LighthousesGame(layout: List<String>, gi: GameInterface<LighthousesGame?, LighthousesGameMove?, LighthousesGameState?>?, gdi: GameDocumentInterface?) : CellsGame<LighthousesGame?, LighthousesGameMove?, LighthousesGameState?>(gi, gdi) {
    var pos2hint: MutableMap<Position?, Int> = HashMap()
    private fun changeObject(move: LighthousesGameMove?, f: F2<LighthousesGameState?, LighthousesGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: LighthousesGameMove?): Boolean {
        return changeObject(move, F2 { obj: LighthousesGameState?, move: LighthousesGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: LighthousesGameMove?): Boolean {
        return changeObject(move, F2 { obj: LighthousesGameState?, move: LighthousesGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): LighthousesObject? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): LighthousesObject? {
        return state()!![row, col]
    }

    fun pos2State(p: Position?): HintState? {
        return state()!!.pos2state[p]
    }

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1))
    }

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch >= '0' && ch <= '9') {
                    val n = ch - '0'
                    pos2hint[p] = n
                }
            }
        }
        val state = LighthousesGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}