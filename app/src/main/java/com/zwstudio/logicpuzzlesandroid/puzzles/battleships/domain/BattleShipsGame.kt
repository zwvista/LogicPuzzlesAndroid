package com.zwstudio.logicpuzzlesandroid.puzzles.battleships.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

import java.util.HashMap

import fj.F2

class BattleShipsGame(layout: List<String>, gi: GameInterface<BattleShipsGame, BattleShipsGameMove, BattleShipsGameState>, gdi: GameDocumentInterface) : CellsGame<BattleShipsGame, BattleShipsGameMove, BattleShipsGameState>(gi, gdi) {

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
    }

    var row2hint: IntArray
    var col2hint: IntArray
    var pos2obj: MutableMap<Position, BattleShipsObject> = HashMap()

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        row2hint = IntArray(rows())
        col2hint = IntArray(cols())

        for (r in 0 until rows() + 1) {
            val str = layout[r]
            for (c in 0 until cols() + 1) {
                val p = Position(r, c)
                val ch = str[c]
                when (ch) {
                    '^' -> pos2obj[p] = BattleShipsObject.BattleShipTop
                    'v' -> pos2obj[p] = BattleShipsObject.BattleShipBottom
                    '<' -> pos2obj[p] = BattleShipsObject.BattleShipLeft
                    '>' -> pos2obj[p] = BattleShipsObject.BattleShipRight
                    '+' -> pos2obj[p] = BattleShipsObject.BattleShipMiddle
                    'o' -> pos2obj[p] = BattleShipsObject.BattleShipUnit
                    '.' -> pos2obj[p] = BattleShipsObject.Marker
                    else -> if (ch in '0'..'9') {
                        val n = ch - '0'
                        if (r == rows())
                            col2hint[c] = n
                        else if (c == cols())
                            row2hint[r] = n
                    }
                }
            }
        }

        val state = BattleShipsGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: BattleShipsGameMove, f: (BattleShipsGameState, BattleShipsGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: BattleShipsGameMove) = changeObject(move, BattleShipsGameState::switchObject)
    fun setObject(move: BattleShipsGameMove) = changeObject(move, BattleShipsGameState::setObject)
    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun getRowState(row: Int) = state().row2state[row]
    fun getColState(col: Int) = state().col2state[col]
}
