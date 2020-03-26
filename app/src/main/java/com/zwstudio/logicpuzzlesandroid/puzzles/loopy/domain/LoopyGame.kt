package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import fj.F2
import java.util.*

class LoopyGame(layout: List<String>, gi: GameInterface<LoopyGame?, LoopyGameMove?, LoopyGameState?>?, gdi: GameDocumentInterface?) : CellsGame<LoopyGame?, LoopyGameMove?, LoopyGameState?>(gi, gdi) {
    var objArray: Array<Array<GridLineObject?>>
    operator fun get(row: Int, col: Int): Array<GridLineObject?> {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position?): Array<GridLineObject?> {
        return get(p!!.row, p.col)
    }

    private fun changeObject(move: LoopyGameMove?, f: F2<LoopyGameState?, LoopyGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: LoopyGameMove?): Boolean {
        return changeObject(move, F2 { obj: LoopyGameState?, move: LoopyGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: LoopyGameMove?): Boolean {
        return changeObject(move, F2 { obj: LoopyGameState?, move: LoopyGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): Array<GridLineObject?>? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): Array<GridLineObject?>? {
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
        size = Position(layout.size / 2 + 1, layout[0].length / 2 + 1)
        objArray = arrayOfNulls(rows() * cols())
        for (i in objArray.indices) {
            objArray[i] = arrayOfNulls(4)
            Arrays.fill(objArray[i], GridLineObject.Empty)
        }
        for (r in 0 until rows()) {
            var str = layout[2 * r]
            for (c in 0 until cols() - 1) {
                val ch = str[2 * c + 1]
                if (ch == '-') {
                    get(r, c + 1)[3] = GridLineObject.Line
                    get(r, c)[1] = get(r, c + 1)[3]
                }
            }
            if (r == rows() - 1) break
            str = layout[2 * r + 1]
            for (c in 0 until cols()) {
                val ch = str[2 * c]
                if (ch == '|') {
                    get(r + 1, c)[0] = GridLineObject.Line
                    get(r, c)[2] = get(r + 1, c)[0]
                }
            }
        }
        val state = LoopyGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}