package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class MagnetsGame(layout: List<String>, gi: GameInterface<MagnetsGame?, MagnetsGameMove?, MagnetsGameState?>?, gdi: GameDocumentInterface?) : CellsGame<MagnetsGame?, MagnetsGameMove?, MagnetsGameState?>(gi, gdi) {
    var row2hint: IntArray
    var col2hint: IntArray
    var areas: MutableList<MagnetsArea> = ArrayList()
    var singles: MutableList<Position> = ArrayList()
    private fun changeObject(move: MagnetsGameMove?, f: F2<MagnetsGameState?, MagnetsGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: MagnetsGameMove?): Boolean {
        return changeObject(move, F2 { obj: MagnetsGameState?, move: MagnetsGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: MagnetsGameMove?): Boolean {
        return changeObject(move, F2 { obj: MagnetsGameState?, move: MagnetsGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): MagnetsObject? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): MagnetsObject? {
        return state()!![row, col]
    }

    fun getRowState(id: Int): HintState? {
        return state()!!.row2state[id]
    }

    fun getColState(id: Int): HintState? {
        return state()!!.col2state[id]
    }

    companion object {
        var offset = arrayOf(
                Position(-1, 0),
                Position(0, 1),
                Position(1, 0),
                Position(0, -1))
    }

    init {
        size = Position(layout.size - 2, layout[0].length - 2)
        row2hint = IntArray(rows() * 2)
        col2hint = IntArray(cols() * 2)
        for (r in 0 until rows() + 2) {
            val str = layout[r]
            for (c in 0 until cols() + 2) {
                val p2 = Position(r, c)
                val ch = str[c]
                if (ch == '.') {
                    areas.add(object : MagnetsArea() {
                        init {
                            p = p2
                            type = MagnetsAreaType.Single
                        }
                    })
                    singles.add(p2)
                } else if (ch == 'H') areas.add(object : MagnetsArea() {
                    init {
                        p = p2
                        type = MagnetsAreaType.Horizontal
                    }
                }) else if (ch == 'V') areas.add(object : MagnetsArea() {
                    init {
                        p = p2
                        type = MagnetsAreaType.Vertical
                    }
                }) else if (ch >= '0' && ch <= '9') {
                    val n = ch - '0'
                    if (r >= rows()) col2hint[c * 2 + r - rows()] = n else if (c >= cols()) row2hint[r * 2 + c - cols()] = n
                }
            }
        }
        val state = MagnetsGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}