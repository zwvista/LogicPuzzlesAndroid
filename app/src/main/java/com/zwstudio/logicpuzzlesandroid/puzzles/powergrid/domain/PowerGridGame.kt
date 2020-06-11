package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PowerGridGame(layout: List<String>, gi: GameInterface<PowerGridGame, PowerGridGameMove, PowerGridGameState>, gdi: GameDocumentInterface) : CellsGame<PowerGridGame, PowerGridGameMove, PowerGridGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var row2hint: IntArray
    var col2hint: IntArray

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        row2hint = IntArray(rows())
        col2hint = IntArray(cols())
        for (r in 0 until rows() + 1) {
            val str = layout[r]
            for (c in 0 until cols() + 1) {
                val ch = str[c]
                if (ch in '0'..'9') {
                    val n = ch - '0'
                    if (r == rows())
                        col2hint[c] = n
                    else if (c == cols())
                        row2hint[r] = n
                }
            }
        }
        val state = PowerGridGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: PowerGridGameMove, f: (PowerGridGameState, PowerGridGameMove) -> Boolean): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state: PowerGridGameState = cloner.deepClone(state())
        val changed = f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states[stateIndex - 1], state)
        }
        return changed
    }

    fun switchObject(move: PowerGridGameMove) = changeObject(move, PowerGridGameState::switchObject)
    fun setObject(move: PowerGridGameMove) = changeObject(move, PowerGridGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun getRowState(row: Int) = state().row2state[row]
    fun getColState(col: Int) = state().col2state[col]
}
