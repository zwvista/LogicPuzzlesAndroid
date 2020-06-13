package com.zwstudio.logicpuzzlesandroid.puzzles.numberlink.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NumberLinkGame(layout: List<String>, gi: GameInterface<NumberLinkGame, NumberLinkGameMove, NumberLinkGameState>, gdi: GameDocumentInterface) : CellsGame<NumberLinkGame, NumberLinkGameMove, NumberLinkGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var pos2hint = mutableMapOf<Position, Int>()
    var pos2rng = mutableMapOf<Int, MutableList<Position>>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == ' ') continue
                val n = if (Character.isDigit(ch)) ch - '0' else ch - 'A' + 10
                pos2hint[p] = n
                val rng = pos2rng[n] ?: mutableListOf()
                rng.add(p)
                pos2rng[n] = rng
            }
        }
        val state = NumberLinkGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: NumberLinkGameMove, f: (NumberLinkGameState, NumberLinkGameMove) -> Boolean): Boolean {
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

    fun setObject(move: NumberLinkGameMove) = changeObject(move, NumberLinkGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2State(p: Position) = currentState.pos2state[p]
}
