package com.zwstudio.logicpuzzlesandroid.puzzles.sheepandwolves

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SheepAndWolvesGame(layout: List<String>, gi: GameInterface<SheepAndWolvesGame, SheepAndWolvesGameMove, SheepAndWolvesGameState>, gdi: GameDocumentInterface) : CellsGame<SheepAndWolvesGame, SheepAndWolvesGameMove, SheepAndWolvesGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        val dirs = intArrayOf(1, 0, 3, 2)
    }

    val pos2hint = mutableMapOf<Position, Int>()
    val sheep = mutableSetOf<Position>()
    val wolves = mutableSetOf<Position>()

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        for (r in 0..<rows - 1) {
            var str = layout[r]
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                when (val ch = str[c]) {
                    in '0'..'9' -> pos2hint[p] = ch - '0'
                    'S' -> sheep.add(p)
                    'W' -> wolves.add(p)
                    else -> {}
                }
            }
        }
        val state = SheepAndWolvesGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}