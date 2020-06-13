package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NurikabeGame(layout: List<String>, gi: GameInterface<NurikabeGame, NurikabeGameMove, NurikabeGameState>, gdi: GameDocumentInterface) : CellsGame<NurikabeGame, NurikabeGameMove, NurikabeGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        var offset2 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(1, 1)
        )
    }

    var pos2hint = mutableMapOf<Position, Int>()

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch in '0'..'9') {
                    val n = ch - '0'
                    pos2hint[p] = n
                }
            }
        }
        val state = NurikabeGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: NurikabeGameMove, f: (NurikabeGameState, NurikabeGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: NurikabeGameMove) = changeObject(move, NurikabeGameState::switchObject)
    fun setObject(move: NurikabeGameMove) = changeObject(move, NurikabeGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}
