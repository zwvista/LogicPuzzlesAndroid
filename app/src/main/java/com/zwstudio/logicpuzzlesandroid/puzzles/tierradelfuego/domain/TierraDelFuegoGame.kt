package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TierraDelFuegoGame(layout: List<String>, gi: GameInterface<TierraDelFuegoGame, TierraDelFuegoGameMove, TierraDelFuegoGameState>, gdi: GameDocumentInterface) : CellsGame<TierraDelFuegoGame, TierraDelFuegoGameMove, TierraDelFuegoGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var pos2hint = mutableMapOf<Position, Char>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ') pos2hint[p] = ch
            }
        }
        val state = TierraDelFuegoGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: TierraDelFuegoGameMove, f: (TierraDelFuegoGameState, TierraDelFuegoGameMove) -> Boolean): Boolean {
        if (canRedo) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(state())
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

    fun switchObject(move: TierraDelFuegoGameMove) = changeObject(move, TierraDelFuegoGameState::switchObject)
    fun setObject(move: TierraDelFuegoGameMove) = changeObject(move, TierraDelFuegoGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
}