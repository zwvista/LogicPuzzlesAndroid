package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2

class KakurasuGame(layout: List<String>, gi: GameInterface<KakurasuGame?, KakurasuGameMove?, KakurasuGameState?>?, gdi: GameDocumentInterface?) : CellsGame<KakurasuGame?, KakurasuGameMove?, KakurasuGameState?>(gi, gdi) {
    override fun isValid(row: Int, col: Int): Boolean {
        return row >= 1 && col >= 1 && row < size.row - 1 && col < size.col - 1
    }

    var row2hint: IntArray
    var col2hint: IntArray
    private fun changeObject(move: KakurasuGameMove?, f: F2<KakurasuGameState?, KakurasuGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: KakurasuGameMove?): Boolean {
        return changeObject(move, F2 { obj: KakurasuGameState?, move: KakurasuGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: KakurasuGameMove?): Boolean {
        return changeObject(move, F2 { obj: KakurasuGameState?, move: KakurasuGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): KakurasuObject? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): KakurasuObject? {
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
        size = Position(layout.size, layout[0].length / 2)
        row2hint = IntArray(rows() * 2)
        col2hint = IntArray(cols() * 2)
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val s = str.substring(c * 2, c * 2 + 2)
                if (s == "  ") continue
                val n = s.trim { it <= ' ' }.toInt()
                if (r == 0 || r == rows() - 1) col2hint[c * 2 + (if (r == 0) 0 else 1)] = n else if (c == 0 || c == cols() - 1) row2hint[r * 2 + (if (c == 0) 0 else 1)] = n
            }
        }
        val state = KakurasuGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}