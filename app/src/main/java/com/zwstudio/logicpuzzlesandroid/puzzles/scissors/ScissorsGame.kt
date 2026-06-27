package com.zwstudio.logicpuzzlesandroid.puzzles.scissors

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ScissorsGame(layout: List<String>, gi: GameInterface<ScissorsGame, ScissorsGameMove, ScissorsGameState>, gdi: GameDocumentInterface) : CellsGame<ScissorsGame, ScissorsGameMove, ScissorsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Square2x2Offset
        const val PUZ_BACK_SLASH = '\\'
        const val PUZ_FRONT_SLASH = '/'
    }

    val objArray: CharArray
    var chMax = '1'
    val numbers: List<Char>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = CharArray(rows * cols) { ' ' }
        for (r in 0..<rows) {
            var str = layout[r]
            for (c in 0..<cols) {
                val ch = str[c]
                this[r, c] = ch
                if (chMax < ch) chMax = ch
            }
        }
        numbers = ('1'..chMax).toList()
        val state = ScissorsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position): Char = currentState[p]
    fun getObject(row: Int, col: Int): Char = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
