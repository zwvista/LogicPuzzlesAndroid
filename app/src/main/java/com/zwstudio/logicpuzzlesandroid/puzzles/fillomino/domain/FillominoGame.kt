package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class FillominoGame(layout: List<String>, gi: GameInterface<FillominoGame?, FillominoGameMove?, FillominoGameState?>?, gdi: GameDocumentInterface?) : CellsGame<FillominoGame?, FillominoGameMove?, FillominoGameState?>(gi, gdi) {
    var areas: List<List<Position>> = ArrayList()
    var pos2area: Map<Position, Int> = HashMap()
    var dots: GridDots
    var chMax: Char
    var objArray: CharArray
    operator fun get(row: Int, col: Int): Char {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position?): Char {
        return get(p!!.row, p.col)
    }

    operator fun set(row: Int, col: Int, obj: Char) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Char) {
        set(p.row, p.col, obj)
    }

    private fun changeObject(move: FillominoGameMove?, f: F2<FillominoGameState?, FillominoGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: FillominoGameMove?): Boolean {
        return changeObject(move, F2 { obj: FillominoGameState?, move: FillominoGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: FillominoGameMove?): Boolean {
        return changeObject(move, F2 { obj: FillominoGameState?, move: FillominoGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): Char {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): Char {
        return state()!![row, col]
    }

    fun getPosState(p: Position?): HintState? {
        return state()!!.pos2state[p]
    }

    fun getDots(): GridDots? {
        return state()!!.dots
    }

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1))
        var offset2 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, 0))
        var dirs = intArrayOf(1, 2, 1, 2)
    }

    init {
        size = Position(layout.size, layout[0].length)
        dots = GridDots(rows() + 1, cols() + 1)
        objArray = CharArray(rows() * cols())
        chMax = ('0'.toInt() + rows()).toChar()
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val ch = str[c]
                set(r, c, ch)
            }
        }
        for (r in 0 until rows()) {
            dots[r, 0, 2] = GridLineObject.Line
            dots[r, cols(), 2] = GridLineObject.Line
        }
        for (c in 0 until cols()) {
            dots[0, c, 1] = GridLineObject.Line
            dots[rows(), c, 1] = GridLineObject.Line
        }
        val state = FillominoGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}