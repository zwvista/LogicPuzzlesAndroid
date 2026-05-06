package com.zwstudio.logicpuzzlesandroid.puzzles.themagicnumber

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TheMagicNumberGame(layout: List<String>, gi: GameInterface<TheMagicNumberGame, TheMagicNumberGameMove, TheMagicNumberGameState>, gdi: GameDocumentInterface) : CellsGame<TheMagicNumberGame, TheMagicNumberGameMove, TheMagicNumberGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val chars = " ABC"
    }

    val objArray: Array<TheMagicNumberObject>
    val shaded = mutableListOf<Position>()
    val symbolCountPerRowCol: Int

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: TheMagicNumberObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: TheMagicNumberObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { TheMagicNumberObject.Empty }
        symbolCountPerRowCol = rows / 3
        for (r in 0 until rows) {
            var str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                val ch2 = if (ch.isLowerCase()) ch.uppercase()[0] else ch
                if (ch.isLowerCase()) shaded.add(p)
                val n = chars.indexOf(ch2)
                this[p] = TheMagicNumberObject.entries[n]
            }
        }
        val state = TheMagicNumberGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
