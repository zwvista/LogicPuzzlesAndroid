package com.zwstudio.logicpuzzlesandroid.puzzles.zenlandscaper

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ZenLandscaperGame(layout: List<String>, gi: GameInterface<ZenLandscaperGame, ZenLandscaperGameMove, ZenLandscaperGameState>, gdi: GameDocumentInterface) : CellsGame<ZenLandscaperGame, ZenLandscaperGameMove, ZenLandscaperGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions8
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
            for (c in 0 until cols)
                this[r, c] = str[c]
        }
        val state = ZenLandscaperGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun getPosState(p: Position) = currentState.pos2state[p]
}