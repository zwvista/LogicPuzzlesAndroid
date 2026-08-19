package com.zwstudio.logicpuzzlesandroid.puzzles.snakeislands

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SnakeIslandsGame(layout: List<String>, gi: GameInterface<SnakeIslandsGame, SnakeIslandsGameMove, SnakeIslandsGameState>, gdi: GameDocumentInterface) : CellsGame<SnakeIslandsGame, SnakeIslandsGameMove, SnakeIslandsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = Position.Square2x2Offset
    }

    val objArray: Array<SnakeIslandsObject>
    val pos2hint = mutableMapOf<Position, Int>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: SnakeIslandsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: SnakeIslandsObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { SnakeIslandsObject.Empty }
        for (r in 0..<rows) {
            var str = layout[r]
            for (c in 0..<cols) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == 'S')
                    this[p] = SnakeIslandsObject.Wall
                else if (ch != ' ') {
                    pos2hint[p] = if (ch.isDigit()) ch - '0' else ch - 'A' + 10
                    this[p] = SnakeIslandsObject.Hint
                }
            }
        }
        val state = SnakeIslandsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
    fun invalid2x2Squares() = currentState.invalid2x2Squares
}
