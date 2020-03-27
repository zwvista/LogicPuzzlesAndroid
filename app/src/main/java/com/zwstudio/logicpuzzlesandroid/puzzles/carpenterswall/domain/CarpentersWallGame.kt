package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2

class CarpentersWallGame(layout: List<String>, gi: GameInterface<CarpentersWallGame, CarpentersWallGameMove, CarpentersWallGameState>, gdi: GameDocumentInterface) : CellsGame<CarpentersWallGame, CarpentersWallGameMove, CarpentersWallGameState>(gi, gdi) {
    var objArray: Array<CarpentersWallObject?>
    operator fun get(row: Int, col: Int): CarpentersWallObject? {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position): CarpentersWallObject? {
        return get(p.row, p.col)
    }

    operator fun set(row: Int, col: Int, obj: CarpentersWallObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: CarpentersWallObject?) {
        set(p.row, p.col, obj)
    }

    private fun changeObject(move: CarpentersWallGameMove?, f: F2<CarpentersWallGameState?, CarpentersWallGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: CarpentersWallGameMove?): Boolean {
        return changeObject(move, F2 { obj: CarpentersWallGameState?, move: CarpentersWallGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: CarpentersWallGameMove?): Boolean {
        return changeObject(move, F2 { obj: CarpentersWallGameState?, move: CarpentersWallGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): CarpentersWallObject? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): CarpentersWallObject? {
        return state()!![row, col]
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
            Position(1, 1))
    }

    init {
        size = Position(layout.size, layout[0].length)
        objArray = arrayOfNulls(rows() * cols())
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch >= '0' && ch <= '9') set(p, object : CarpentersWallCornerObject() {
                    init {
                        tiles = ch - '0'
                        state = HintState.Normal
                    }
                }) else if (ch == 'O') set(p, object : CarpentersWallCornerObject() {
                    init {
                        tiles = 0
                        state = HintState.Normal
                    }
                }) else if (ch == '^') set(p, CarpentersWallUpObject()) else if (ch == 'v') set(p, CarpentersWallDownObject()) else if (ch == '<') set(p, CarpentersWallLeftObject()) else if (ch == '>') set(p, CarpentersWallRightObject()) else set(p, CarpentersWallEmptyObject())
            }
        }
        val state = CarpentersWallGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}