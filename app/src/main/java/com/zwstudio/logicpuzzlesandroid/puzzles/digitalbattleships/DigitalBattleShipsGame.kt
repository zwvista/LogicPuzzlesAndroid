package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DigitalBattleShipsGame(layout: List<String>, gi: GameInterface<DigitalBattleShipsGame, DigitalBattleShipsGameMove, DigitalBattleShipsGameState>, gdi: GameDocumentInterface) : CellsGame<DigitalBattleShipsGame, DigitalBattleShipsGameMove, DigitalBattleShipsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Directions8
    }

    val objArray: IntArray
    val row2hint: IntArray
    val col2hint: IntArray

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size - 1, layout[0].length / 2 - 1)
        objArray = IntArray(rows * cols)
        row2hint = IntArray(rows)
        col2hint = IntArray(cols)
        for (r in 0 until rows + 1) {
            var str = layout[r]
            for (c in 0 until cols + 1) {
                val s = str.substring(c * 2, c * 2 + 2)
                if (s == "  ") continue
                val n = s.trim(' ').toInt()
                if (r == rows)
                    col2hint[c] = n
                else if (c == cols)
                    row2hint[r] = n
                else
                    this[r, c] = n
            }
        }
        val state = DigitalBattleShipsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun row2state(row: Int) = currentState.row2state[row]
    fun col2state(col: Int) = currentState.col2state[col]
}
