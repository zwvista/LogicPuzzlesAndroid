package com.zwstudio.logicpuzzlesandroid.puzzles.snake

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SnakeGame(layout: List<String>, gi: GameInterface<SnakeGame, SnakeGameMove, SnakeGameState>, gdi: GameDocumentInterface) : CellsGame<SnakeGame, SnakeGameMove, SnakeGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
    }

    val row2hint: IntArray
    val col2hint: IntArray
    val pos2snake = mutableListOf<Position>()

    init {
        size = Position(layout.size - 1, layout[0].length - 1)
        row2hint = IntArray(rows)
        col2hint = IntArray(cols)
        for (r in 0..<rows + 1) {
            val str = layout[r]
            for (c in 0..<cols + 1) {
                val p = Position(r, c)
                when (val ch = str[c]) {
                    'S' -> pos2snake.add(p)
                    in '0'..'9' -> {
                        val n = ch - '0'
                        if (r == rows)
                            col2hint[c] = n
                        else if (c == cols)
                            row2hint[r] = n
                    }
                    else -> {
                        if (r == rows && c == cols) {
                            //
                        } else if (r == rows)
                            col2hint[c] = -1
                        else if (c == cols)
                            row2hint[r] = -1
                    }
                }
            }
        }
        val state = SnakeGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun row2state(row: Int) = currentState.row2state[row]
    fun col2state(col: Int) = currentState.col2state[col]
}
