package com.zwstudio.logicpuzzlesandroid.puzzles.battleships

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

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
    var pos2obj = mutableMapOf<Position, BattleShipsObject>()

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        row2hint = IntArray(rows)
        col2hint = IntArray(cols)

        for (r in 0 until rows + 1) {
            val str = layout[r]
            for (c in 0 until cols + 1) {
                val p = Position(r, c)
                when (val ch = str[c]) {
                    '^' -> pos2obj[p] = BattleShipsObject.BattleShipTop
                    'v' -> pos2obj[p] = BattleShipsObject.BattleShipBottom
                    '<' -> pos2obj[p] = BattleShipsObject.BattleShipLeft
                    '>' -> pos2obj[p] = BattleShipsObject.BattleShipRight
                    '+' -> pos2obj[p] = BattleShipsObject.BattleShipMiddle
                    'o' -> pos2obj[p] = BattleShipsObject.BattleShipUnit
                    '.' -> pos2obj[p] = BattleShipsObject.Marker
                    else -> if (ch in '0'..'9') {
                        val n = ch - '0'
                        if (r == rows)
                            col2hint[c] = n
                        else if (c == cols)
                            row2hint[r] = n
                    }
                }
            }
        }

        val state = BattleShipsGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    fun switchObject(move: BattleShipsGameMove) = changeObject(move, BattleShipsGameState::switchObject)
    fun setObject(move: BattleShipsGameMove) = changeObject(move, BattleShipsGameState::setObject)
    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getRowState(row: Int) = currentState.row2state[row]
    fun getColState(col: Int) = currentState.col2state[col]
}
