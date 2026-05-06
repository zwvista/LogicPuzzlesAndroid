package com.zwstudio.logicpuzzlesandroid.puzzles.pipemania

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PipemaniaGame(layout: List<String>, gi: GameInterface<PipemaniaGame, PipemaniaGameMove, PipemaniaGameState>, gdi: GameDocumentInterface) : CellsGame<PipemaniaGame, PipemaniaGameMove, PipemaniaGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val objArray: Array<PipemaniaObject>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: PipemaniaObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: PipemaniaObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { PipemaniaObject.Empty }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols)
                this[r, c] = when (str[c]) {
                    '3' -> PipemaniaObject.UpRight
                    '6' -> PipemaniaObject.DownRight
                    'C' -> PipemaniaObject.LeftDown
                    '9' -> PipemaniaObject.LeftUp
                    'A' -> PipemaniaObject.Horizontal
                    '5' -> PipemaniaObject.Vertical
                    'F' -> PipemaniaObject.Cross
                    else -> PipemaniaObject.Empty
                }
        }
        val state = PipemaniaGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}
