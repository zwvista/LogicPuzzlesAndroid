package com.zwstudio.logicpuzzlesandroid.puzzles.landscaper

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LandscaperGame(layout: List<String>, gi: GameInterface<LandscaperGame, LandscaperGameMove, LandscaperGameState>, gdi: GameDocumentInterface) : CellsGame<LandscaperGame, LandscaperGameMove, LandscaperGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    var objArray: Array<LandscaperObject>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: LandscaperObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: LandscaperObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { LandscaperObject.Empty }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols)
                when (str[c]) {
                    'T' -> this[r, c] = LandscaperObject.Tree
                    'F' -> this[r, c] = LandscaperObject.Flower
                }
        }
        val state = LandscaperGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
