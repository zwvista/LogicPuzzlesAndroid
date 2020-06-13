package com.zwstudio.logicpuzzlesandroid.puzzles.tents

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TentsGame(layout: List<String>, gi: GameInterface<TentsGame, TentsGameMove, TentsGameState>, gdi: GameDocumentInterface) : CellsGame<TentsGame, TentsGameMove, TentsGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        var offset2 = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1)
        )
    }

    var row2hint: IntArray
    var col2hint: IntArray
    var pos2tree = mutableListOf<Position>()

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        row2hint = IntArray(rows)
        col2hint = IntArray(cols)
        for (r in 0 until rows + 1) {
            val str = layout[r]
            for (c in 0 until cols + 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == 'T')
                    pos2tree.add(p)
                else if (ch in '0'..'9') {
                    val n = ch - '0'
                    if (r == rows)
                        col2hint[c] = n
                    else if (c == cols)
                        row2hint[r] = n
                }
            }
        }
        val state = TentsGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: TentsGameMove, f: (TentsGameState, TentsGameMove) -> Boolean): Boolean {
        if (canRedo) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(currentState)
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

    fun switchObject(move: TentsGameMove) = changeObject(move, TentsGameState::switchObject)
    fun setObject(move: TentsGameMove) = changeObject(move, TentsGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getRowState(row: Int) = currentState.row2state[row]
    fun getColState(col: Int) = currentState.col2state[col]
}