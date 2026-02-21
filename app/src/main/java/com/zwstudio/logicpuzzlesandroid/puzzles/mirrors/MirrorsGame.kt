package com.zwstudio.logicpuzzlesandroid.puzzles.mirrors

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MirrorsGame(layout: List<String>, gi: GameInterface<MirrorsGame, MirrorsGameMove, MirrorsGameState>, gdi: GameDocumentInterface) : CellsGame<MirrorsGame, MirrorsGameMove, MirrorsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    var spots = mutableListOf<Position>()
    var objArray: Array<MirrorsObject>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: MirrorsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: MirrorsObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { MirrorsObject.Empty }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                this[p] = when (str[c]) {
                    'O' -> MirrorsObject.Block
                    '3' -> MirrorsObject.UpRight
                    '6' -> MirrorsObject.DownRight
                    'C' -> MirrorsObject.LeftDown
                    '9' -> MirrorsObject.LeftUp
                    'A' -> MirrorsObject.Horizontal
                    '5' -> MirrorsObject.Vertical
                    'S' -> MirrorsObject.Spot
                    else -> MirrorsObject.Empty
                }
                if (this[p] == MirrorsObject.Spot)
                    spots.add(p)
            }
        }
        val state = MirrorsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2dirs() = currentState.pos2dirs
}
