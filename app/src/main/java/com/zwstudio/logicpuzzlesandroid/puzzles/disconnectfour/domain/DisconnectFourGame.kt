package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DisconnectFourGame(layout: List<String>, gi: GameInterface<DisconnectFourGame, DisconnectFourGameMove, DisconnectFourGameState>, gdi: GameDocumentInterface) : CellsGame<DisconnectFourGame, DisconnectFourGameMove, DisconnectFourGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var objArray: Array<DisconnectFourObject>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: DisconnectFourObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: DisconnectFourObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { DisconnectFourObject.Empty }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols)
                when (str[c]) {
                    'Y' -> this[r, c] = DisconnectFourObject.Yellow
                    'R' -> this[r, c] = DisconnectFourObject.Red
                }
        }
        val state = DisconnectFourGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: DisconnectFourGameMove, f: (DisconnectFourGameState, DisconnectFourGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: DisconnectFourGameMove) = changeObject(move, DisconnectFourGameState::switchObject)
    fun setObject(move: DisconnectFourGameMove) = changeObject(move, DisconnectFourGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun pos2State(p: Position) = state().pos2state[p]
}
