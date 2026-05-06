package com.zwstudio.logicpuzzlesandroid.puzzles.fencingsheep

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FencingSheepGame(layout: List<String>, gi: GameInterface<FencingSheepGame, FencingSheepGameMove, FencingSheepGameState>, gdi: GameDocumentInterface) : CellsGame<FencingSheepGame, FencingSheepGameMove, FencingSheepGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        val dirs = intArrayOf(1, 0, 3, 2)
        const val PUZ_POST = 'O'
        const val PUZ_SHEEP = 'S'
        const val PUZ_WOLF = 'W'
    }

    val objArray: MutableList<MutableList<GridLineObject>>
    val wolves = mutableListOf<Position>()
    val sheep = mutableListOf<Position>()
    val posts = mutableListOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        size = Position(layout.size / 2 + 1, layout[0].length / 2 + 1)
        objArray = MutableList(rows * cols) { MutableList(4) { GridLineObject.Empty } }
        for (r in 0..<rows) {
            var str = layout[r * 2]
            for (c in 0..<cols) {
                val ch = str[c * 2]
                if (ch == PUZ_POST)
                    posts.add(Position(r, c))
                if (c == cols - 1) break
                val ch2 = str[c * 2 + 1]
                if (ch2 == '-') {
                    this[r, c][1] = GridLineObject.Line
                    this[r, c + 1][3] = GridLineObject.Line
                }
            }
            if (r == rows - 1) break
            str = layout[r * 2 + 1]
            for (c in 0..<cols) {
                val ch = str[c * 2]
                if (ch == '|') {
                    this[r, c][2] = GridLineObject.Line
                    this[r + 1, c][0] = GridLineObject.Line
                }
                if (c == cols - 1) break
                val p = Position(r, c)
                when(str[c * 2 + 1]) {
                    PUZ_SHEEP -> sheep.add(p)
                    PUZ_WOLF -> wolves.add(p)
                }
            }
        }
        val state = FencingSheepGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
}