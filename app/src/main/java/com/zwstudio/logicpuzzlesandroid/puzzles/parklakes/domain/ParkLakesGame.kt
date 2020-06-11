package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import java.util.*

class ParkLakesGame(layout: List<String>, gi: GameInterface<ParkLakesGame, ParkLakesGameMove, ParkLakesGameState>, gdi: GameDocumentInterface) : CellsGame<ParkLakesGame, ParkLakesGameMove, ParkLakesGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length / 2)
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val s = str.substring(c * 2, c * 2 + 2)
                if (s != "  ") pos2hint[p] = if (s == " ?") -1 else s.trim(' ').toInt()
            }
        }
        val state = ParkLakesGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: ParkLakesGameMove, f: (ParkLakesGameState, ParkLakesGameMove) -> Boolean): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state: ParkLakesGameState = cloner.deepClone(state())
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

    fun switchObject(move: ParkLakesGameMove) = changeObject(move, ParkLakesGameState::switchObject)
    fun setObject(move: ParkLakesGameMove) = changeObject(move, ParkLakesGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
}
