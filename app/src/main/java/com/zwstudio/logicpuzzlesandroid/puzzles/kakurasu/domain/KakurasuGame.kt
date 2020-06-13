package com.zwstudio.logicpuzzlesandroid.puzzles.kakurasu.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class KakurasuGame(layout: List<String>, gi: GameInterface<KakurasuGame, KakurasuGameMove, KakurasuGameState>, gdi: GameDocumentInterface) : CellsGame<KakurasuGame, KakurasuGameMove, KakurasuGameState>(gi, gdi) {
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

    override fun isValid(row: Int, col: Int) = row >= 1 && col >= 1 && row < size.row - 1 && col < size.col - 1

    init {
        size = Position(layout.size, layout[0].length / 2)
        row2hint = IntArray(rows * 2)
        col2hint = IntArray(cols * 2)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val s = str.substring(c * 2, c * 2 + 2)
                if (s == "  ") continue
                val n = s.trim(' ').toInt()
                if (r == 0 || r == rows - 1)
                    col2hint[c * 2 + (if (r == 0) 0 else 1)] = n
                else if (c == 0 || c == cols - 1)
                    row2hint[r * 2 + (if (c == 0) 0 else 1)] = n
            }
        }
        val state = KakurasuGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: KakurasuGameMove, f: (KakurasuGameState, KakurasuGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: KakurasuGameMove) = changeObject(move, KakurasuGameState::switchObject)
    fun setObject(move: KakurasuGameMove) = changeObject(move, KakurasuGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getRowState(row: Int) = currentState.row2state[row]
    fun getColState(col: Int) = currentState.col2state[col]
}
