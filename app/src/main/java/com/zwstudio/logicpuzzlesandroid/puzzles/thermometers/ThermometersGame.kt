package com.zwstudio.logicpuzzlesandroid.puzzles.thermometers

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ThermometersGame(layout: List<String>, gi: GameInterface<ThermometersGame, ThermometersGameMove, ThermometersGameState>, gdi: GameDocumentInterface) : CellsGame<ThermometersGame, ThermometersGameMove, ThermometersGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val bulbs = "^>v<"
        const val parts = "URDLurdl|-"
    }

    var row2hint: IntArray
    var col2hint: IntArray
    var objArray: CharArray
    var thermometers = mutableListOf<MutableList<Position>>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        row2hint = IntArray(rows)
        col2hint = IntArray(cols)
        objArray = CharArray(rows * cols)
        for (r in 0 until rows + 1) {
            val str = layout[r]
            for (c in 0 until cols + 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch.isDigit()) {
                    val n = ch - '0'
                    if (r == rows)
                        col2hint[c] = n
                    else if (c == cols)
                        row2hint[r] = n
                } else if (ch in bulbs)
                    thermometers.add(mutableListOf(p))
            }
        }
        for (thermometer in thermometers) {
            val p = thermometer[0]
            val (r, c) = p
            val ch = layout[r][c]
            val d = bulbs.indexOf(ch)
            this[p] = parts[d]
            val d2 = (d + 2) % 4
            val os = offset[d2]
            var p2 = p + os
            while (isValid(p2)) {
                thermometer.add(p2)
                val ch2 = layout[p2.row][p2.col]
                if (ch2 == '+')
                    this[p2] = if (d % 2 == 0) '|' else '-'
                else if (ch2 == 'o') {
                    this[p2] = parts[d2 + 4]
                    break
                }
                p2 += os
            }
        }
        val state = ThermometersGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getRowState(row: Int) = currentState.row2state[row]
    fun getColState(col: Int) = currentState.col2state[col]
}