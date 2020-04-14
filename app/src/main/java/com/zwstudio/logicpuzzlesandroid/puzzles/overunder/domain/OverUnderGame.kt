package com.zwstudio.logicpuzzlesandroid.puzzles.overunder.domainimport

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class OverUnderGame(layout: List<String>, gi: GameInterface<OverUnderGame, OverUnderGameMove, OverUnderGameState>, gdi: GameDocumentInterface) : CellsGame<OverUnderGame, OverUnderGameMove, OverUnderGameState>(gi, gdi) {
    var objArray: Array<Array<GridLineObject?>>
    var pos2hint: MutableMap<Position, Int> = HashMap()
    var areaSize = 0
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject?>) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: Array<GridLineObject?>) {
        set(p.row, p.col, obj)
    }

    private fun changeObject(move: OverUnderGameMove, f: (OverUnderGameState, OverUnderGameMove) -> Boolean): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state: OverUnderGameState = cloner.deepClone(state())
        val changed = f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states.get(stateIndex - 1), state)
        }
        return changed
    }

    fun switchObject(move: OverUnderGameMove) = changeObject(move, OverUnderGameState::switchObject)
    fun setObject(move: OverUnderGameMove) = changeObject(move, OverUnderGameState::setObject)

    fun getObject(p: Position?) = state().get(p)

    fun getObject(row: Int, col: Int) = state().get(row, col)

    fun pos2State(p: Position?) = state().pos2state.get(p)

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1))
        var offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0))
        var dirs = intArrayOf(1, 0, 3, 2)
    }

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        objArray = arrayOfNulls<Array<GridLineObject?>>(rows() * cols())
        for (i in objArray.indices) {
            objArray[i] = arrayOfNulls<GridLineObject>(4)
            Arrays.fill(objArray[i], GridLineObject.Empty)
        }
        for (r in 0 until rows() - 1) {
            val str = layout[r]
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == ' ') continue
                val n = ch - '0'
                pos2hint[p] = n
            }
        }
        areaSize = (rows() - 1) * (cols() - 1) / pos2hint.size
        for (r in 0 until rows() - 1) {
            get(r, 0)[2] = GridLineObject.Line
            get(r + 1, 0)[0] = GridLineObject.Line
            get(r, cols() - 1)[2] = GridLineObject.Line
            get(r + 1, cols() - 1)[0] = GridLineObject.Line
        }
        for (c in 0 until cols() - 1) {
            get(0, c)[1] = GridLineObject.Line
            get(0, c + 1)[3] = GridLineObject.Line
            get(rows() - 1, c)[1] = GridLineObject.Line
            get(rows() - 1, c + 1)[3] = GridLineObject.Line
        }
        val state = OverUnderGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}