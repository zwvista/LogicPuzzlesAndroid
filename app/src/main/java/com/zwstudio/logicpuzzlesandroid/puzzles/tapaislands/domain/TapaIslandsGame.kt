package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TapaIslandsGame(layout: List<String>, gi: GameInterface<TapaIslandsGame, TapaIslandsGameMove, TapaIslandsGameState>, gdi: GameDocumentInterface) : CellsGame<TapaIslandsGame, TapaIslandsGameMove, TapaIslandsGameState>(gi, gdi) {
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
                val s = str.substring(c * 4, c * 4 + 4).trim(' ')
                if (s.isEmpty()) continue
                val hint = mutableListOf<Int>()
                for (ch in s.toCharArray()) {
                    if (ch == '?' || ch in '0'..'9') {
                        val n = if (ch == '?') -1 else ch - '0'
                        hint.add(n)
                    }
                }
                pos2hint[p] = hint
            }
        }
        val state = TapaIslandsGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: TapaIslandsGameMove, f: (TapaIslandsGameState, TapaIslandsGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: TapaIslandsGameMove) = changeObject(move, TapaIslandsGameState::switchObject)
    fun setObject(move: TapaIslandsGameMove) = changeObject(move, TapaIslandsGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
}
