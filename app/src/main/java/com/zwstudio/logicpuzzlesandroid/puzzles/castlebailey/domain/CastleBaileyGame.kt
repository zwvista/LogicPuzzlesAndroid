package com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CastleBaileyGame(layout: List<String>, gi: GameInterface<CastleBaileyGame, CastleBaileyGameMove, CastleBaileyGameState>, gdi: GameDocumentInterface) : CellsGame<CastleBaileyGame, CastleBaileyGameMove, CastleBaileyGameState>(gi, gdi) {
    companion object {
        val offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        val offset2 = arrayOf(
            Position(-1, -1),
            Position(-1, 0),
            Position(0, 0),
            Position(0, -1)
        )
    }

    var pos2hint = mutableMapOf<Position, Int>();

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        for (r in 0 until rows() + 1) {
            val str = layout[r]
            for (c in 0 until cols() + 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ')
                    pos2hint[p] = ch - '0'
            }
        }
        val state = CastleBaileyGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: CastleBaileyGameMove, f: ((CastleBaileyGameState, CastleBaileyGameMove) -> Boolean)): Boolean {
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

    fun switchObject(move: CastleBaileyGameMove) = changeObject(move, CastleBaileyGameState::switchObject)
    fun setObject(move: CastleBaileyGameMove) = changeObject(move, CastleBaileyGameState::setObject)

    fun getObject(p: Position): CastleBaileyObject = state()[p]
    fun getObject(row: Int, col: Int): CastleBaileyObject = state()[row, col]
    fun getPosState(p: Position) = state().pos2state[p]
}
