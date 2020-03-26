package com.zwstudio.logicpuzzlesandroid.puzzles.kakuro.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class KakuroGame(layout: List<String>, gi: GameInterface<KakuroGame?, KakuroGameMove?, KakuroGameState?>?, gdi: GameDocumentInterface?) : CellsGame<KakuroGame?, KakuroGameMove?, KakuroGameState?>(gi, gdi) {
    var pos2horzHint: MutableMap<Position, Int> = HashMap()
    var pos2vertHint: MutableMap<Position, Int> = HashMap()
    var pos2num: MutableMap<Position, Int> = HashMap()
    private fun changeObject(move: KakuroGameMove?, f: F2<KakuroGameState?, KakuroGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: KakuroGameMove?): Boolean {
        return changeObject(move, F2 { obj: KakuroGameState?, move: KakuroGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: KakuroGameMove?): Boolean {
        return changeObject(move, F2 { obj: KakuroGameState?, move: KakuroGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): Int? {
        return state()!![p]
    }

    fun getHorzState(p: Position?): HintState? {
        return state()!!.pos2horzHint[p]
    }

    fun getVertState(p: Position?): HintState? {
        return state()!!.pos2vertHint[p]
    }

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1))
    }

    init {
        size = Position(layout.size, layout[0].length / 4)
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val s = str.substring(c * 4, c * 4 + 4)
                if (s == "    ") pos2num[p] = 0 else {
                    val s1 = s.substring(0, 2)
                    val s2 = s.substring(2, 4)
                    if (s1 != "00") pos2vertHint[p] = s1.toInt()
                    if (s2 != "00") pos2horzHint[p] = s2.toInt()
                }
            }
        }
        val state = KakuroGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}