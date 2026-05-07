package com.zwstudio.logicpuzzlesandroid.puzzles.thegreylabyrinth

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TheGreyLabyrinthGame(layout: List<String>, gi: GameInterface<TheGreyLabyrinthGame, TheGreyLabyrinthGameMove, TheGreyLabyrinthGameState>, gdi: GameDocumentInterface) : CellsGame<TheGreyLabyrinthGame, TheGreyLabyrinthGameMove, TheGreyLabyrinthGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val chars = " T^>v<"
    }

    val objArray: Array<TheGreyLabyrinthObject>
    var treasure = Position.Zero

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: TheGreyLabyrinthObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: TheGreyLabyrinthObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { TheGreyLabyrinthObject.Empty }
        for (r in 0..<rows) {
            var str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val ch = str[c]
                val n = chars.indexOf(ch)
                this[p] = TheGreyLabyrinthObject.entries[n]
                if (str[c] == 'T')
                    treasure = p
            }
        }
        val state = TheGreyLabyrinthGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
