package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LoopyGame(layout: List<String>, gi: GameInterface<LoopyGame, LoopyGameMove, LoopyGameState>, gdi: GameDocumentInterface) : CellsGame<LoopyGame, LoopyGameMove, LoopyGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var objArray: Array<Array<GridLineObject>>

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        size = Position(layout.size / 2 + 1, layout[0].length / 2 + 1)
        objArray = Array(rows() * cols()) { Array(4) { GridLineObject.Empty } }
        for (r in 0 until rows()) {
            var str = layout[2 * r]
            for (c in 0 until cols() - 1) {
                val ch = str[2 * c + 1]
                if (ch == '-') {
                    this[r, c + 1][3] = GridLineObject.Line
                    this[r, c][1] = this[r, c + 1][3]
                }
            }
            if (r == rows() - 1) break
            str = layout[2 * r + 1]
            for (c in 0 until cols()) {
                val ch = str[2 * c]
                if (ch == '|') {
                    this[r + 1, c][0] = GridLineObject.Line
                    this[r, c][2] = this[r + 1, c][0]
                }
            }
        }
        val state = LoopyGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: LoopyGameMove, f: (LoopyGameState, LoopyGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: LoopyGameMove) = changeObject(move, LoopyGameState::switchObject)
    fun setObject(move: LoopyGameMove) = changeObject(move, LoopyGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
}