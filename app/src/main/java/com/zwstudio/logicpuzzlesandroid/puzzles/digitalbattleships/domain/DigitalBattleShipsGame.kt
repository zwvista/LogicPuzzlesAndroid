package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2

class DigitalBattleShipsGame(layout: List<String>, gi: GameInterface<DigitalBattleShipsGame?, DigitalBattleShipsGameMove?, DigitalBattleShipsGameState?>?, gdi: GameDocumentInterface?) : CellsGame<DigitalBattleShipsGame?, DigitalBattleShipsGameMove?, DigitalBattleShipsGameState?>(gi, gdi) {
    var objArray: IntArray
    var row2hint: IntArray
    var col2hint: IntArray
    operator fun get(row: Int, col: Int): Int {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position): Int {
        return get(p.row, p.col)
    }

    operator fun set(row: Int, col: Int, obj: Int) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Int) {
        set(p.row, p.col, obj)
    }

    private fun changeObject(move: DigitalBattleShipsGameMove?, f: F2<DigitalBattleShipsGameState?, DigitalBattleShipsGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: DigitalBattleShipsGameMove?): Boolean {
        return changeObject(move, F2 { obj: DigitalBattleShipsGameState?, move: DigitalBattleShipsGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: DigitalBattleShipsGameMove?): Boolean {
        return changeObject(move, F2 { obj: DigitalBattleShipsGameState?, move: DigitalBattleShipsGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): DigitalBattleShipsObject? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): DigitalBattleShipsObject? {
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
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1))
    }

    init {
        size = Position(layout.size - 1, layout[0].length / 2 - 1)
        objArray = IntArray(rows() * cols())
        row2hint = IntArray(rows())
        col2hint = IntArray(cols())
        for (r in 0 until rows() + 1) {
            val str = layout[r]
            for (c in 0 until cols() + 1) {
                val s = str.substring(c * 2, c * 2 + 2)
                if (s == "  ") continue
                val n = s.trim { it <= ' ' }.toInt()
                if (r == rows()) col2hint[c] = n else if (c == cols()) row2hint[r] = n else set(r, c, n)
            }
        }
        val state = DigitalBattleShipsGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}