package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class BusySeasGame(layout: List<String>, gi: GameInterface<BusySeasGame, BusySeasGameMove, BusySeasGameState>, gdi: GameDocumentInterface) : CellsGame<BusySeasGame, BusySeasGameMove, BusySeasGameState>(gi, gdi) {
    var pos2hint: MutableMap<Position?, Int> = HashMap()
    private fun changeObject(move: BusySeasGameMove?, f: F2<BusySeasGameState?, BusySeasGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: BusySeasGameMove?): Boolean {
        return changeObject(move, F2 { obj: BusySeasGameState?, move: BusySeasGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: BusySeasGameMove?): Boolean {
        return changeObject(move, F2 { obj: BusySeasGameState?, move: BusySeasGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): BusySeasObject? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): BusySeasObject? {
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
        val state = BusySeasGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}