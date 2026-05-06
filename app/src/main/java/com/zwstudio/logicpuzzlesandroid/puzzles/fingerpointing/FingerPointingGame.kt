package com.zwstudio.logicpuzzlesandroid.puzzles.fingerpointing

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FingerPointingGame(layout: List<String>, gi: GameInterface<FingerPointingGame, FingerPointingGameMove, FingerPointingGameState>, gdi: GameDocumentInterface) : CellsGame<FingerPointingGame, FingerPointingGameMove, FingerPointingGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        const val chars = "^>v<"
        const val PUZ_BLOCK = '@'
    }

    val pos2hint = mutableMapOf<Position, Int>()
    val objArray: Array<FingerPointingObject>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: FingerPointingObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: FingerPointingObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { FingerPointingObject.Empty }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == PUZ_BLOCK)
                    this[p] = FingerPointingObject.Block
                else if (ch != ' ') {
                    val dir = chars.indexOf(ch)
                    if (dir != -1)
                        this[p] = FingerPointingObject.entries[dir + FingerPointingObject.Up.ordinal]
                    else {
                        pos2hint[p] = if (ch.isDigit()) ch - '0' else ch - 'A' + 10
                        this[p] = FingerPointingObject.Hint
                    }
                }
            }
        }
        val state = FingerPointingGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}