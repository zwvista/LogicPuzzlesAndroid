package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class LightBattleShipsGame(layout: List<String>, gi: GameInterface<LightBattleShipsGame, LightBattleShipsGameMove, LightBattleShipsGameState>, gdi: GameDocumentInterface) : CellsGame<LightBattleShipsGame, LightBattleShipsGameMove, LightBattleShipsGameState>(gi, gdi) {
    var pos2hint: MutableMap<Position?, Int> = HashMap()
    var pos2obj: MutableMap<Position?, LightBattleShipsObject?> = HashMap()
    private fun changeObject(move: LightBattleShipsGameMove, f: (LightBattleShipsGameState, LightBattleShipsGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: LightBattleShipsGameMove) = changeObject(move, LightBattleShipsGameState::switchObject)
    fun setObject(move: LightBattleShipsGameMove) = changeObject(move, LightBattleShipsGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int)  = state()[row, col]
    fun pos2State(p: Position?): HintState? {
        return state()!!.pos2state[p]
    }

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1))
    }

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = str[c]
                when (ch) {
                    '^' -> pos2obj[p] = LightBattleShipsBattleShipTopObject()
                    'v' -> pos2obj[p] = LightBattleShipsBattleShipBottomObject()
                    '<' -> pos2obj[p] = LightBattleShipsBattleShipLeftObject()
                    '>' -> pos2obj[p] = LightBattleShipsBattleShipRightObject()
                    '+' -> pos2obj[p] = LightBattleShipsBattleShipMiddleObject()
                    'o' -> pos2obj[p] = LightBattleShipsBattleShipUnitObject()
                    '.' -> pos2obj[p] = LightBattleShipsMarkerObject()
                    else -> if (ch >= '0' && ch <= '9') {
                        val n = ch - '0'
                        pos2hint[Position(r, c)] = n
                    }
                }
            }
        }
        val state = LightBattleShipsGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}