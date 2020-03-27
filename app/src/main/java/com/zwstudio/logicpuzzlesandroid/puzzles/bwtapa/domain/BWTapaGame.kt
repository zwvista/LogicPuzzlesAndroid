package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BWTapaGame(layout: List<String>, gi: GameInterface<BWTapaGame, BWTapaGameMove, BWTapaGameState>, gdi: GameDocumentInterface) : CellsGame<BWTapaGame, BWTapaGameMove, BWTapaGameState>(gi, gdi) {

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1)
        )
        var offset2 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(1, 1)
        )
    }

    var pos2hint = mutableMapOf<Position, List<Int>>()

    init {
        size = Position(layout.size, layout[0].length / 4)
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val s = str.substring(c * 4, c * 4 + 4).trim { it <= ' ' }
                if (s.isEmpty()) continue
                val hint: MutableList<Int> = ArrayList()
                for (ch in s.toCharArray()) {
                    if (ch == '?' || ch >= '0' && ch <= '9') {
                        val n = if (ch == '?') -1 else ch - '0'
                        hint.add(n)
                    }
                }
                pos2hint[p] = hint
            }
        }
        val state = BWTapaGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: BWTapaGameMove, f: (BWTapaGameState, BWTapaGameMove) -> Boolean): Boolean {
        if (canRedo()) {
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

    fun switchObject(move: BWTapaGameMove) = changeObject(move, BWTapaGameState::switchObject)
    fun setObject(move: BWTapaGameMove) = changeObject(move, BWTapaGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
}