package com.zwstudio.logicpuzzlesandroid.puzzles.hitori

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HitoriGame(layout: List<String>, gi: GameInterface<HitoriGame, HitoriGameMove, HitoriGameState>, gdi: GameDocumentInterface) : CellsGame<HitoriGame, HitoriGameMove, HitoriGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var objArray: CharArray

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = CharArray(rows * cols)
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val ch = str[c]
                if (ch in '0'..'9')
                    this[r, c] = ch
            }
        }
        val state = HitoriGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    fun switchObject(move: HitoriGameMove) = changeObject(move, HitoriGameState::switchObject)
    fun setObject(move: HitoriGameMove) = changeObject(move, HitoriGameState::setObject)

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]

    fun getRowHint(row: Int) = currentState.row2hint[row]
    fun getColHint(col: Int) = currentState.col2hint[col]
}
