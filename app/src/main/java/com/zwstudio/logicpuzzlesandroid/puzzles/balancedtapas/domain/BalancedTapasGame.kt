package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BalancedTapasGame(layout: List<String>, leftPart: String, gi: GameInterface<BalancedTapasGame, BalancedTapasGameMove, BalancedTapasGameState>, gdi: GameDocumentInterface) : CellsGame<BalancedTapasGame, BalancedTapasGameMove, BalancedTapasGameState>(gi, gdi) {
    companion object {
        val offset = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1)
        )
        val offset2 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(1, 1)
        )
    }

    var pos2hint = mutableMapOf<Position, List<Int>>()
    var left = 0
    var right = 0

    init {
        size = Position(layout.size, layout[0].length / 4)
        left = leftPart[0] - '0'
        right = left
        if (leftPart.length > 1) left++
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val s = str.substring(c * 4, c * 4 + 4).trim(' ')
                if (s.isEmpty()) continue
                val hint = mutableListOf<Int>()
                for (ch in s)
                    when (ch) {
                        in '0'..'9' -> hint.add(ch - '0')
                        '?' -> hint.add(-1)
                    }
                pos2hint[p] = hint
            }
        }
        val state = BalancedTapasGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: BalancedTapasGameMove, f: (BalancedTapasGameState, BalancedTapasGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: BalancedTapasGameMove) = changeObject(move, BalancedTapasGameState::switchObject)
    fun setObject(move: BalancedTapasGameMove) = changeObject(move, BalancedTapasGameState::setObject)
    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
}
