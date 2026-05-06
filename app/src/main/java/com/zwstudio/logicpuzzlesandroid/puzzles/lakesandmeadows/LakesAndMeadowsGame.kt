package com.zwstudio.logicpuzzlesandroid.puzzles.lakesandmeadows

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridDots
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LakesAndMeadowsGame(layout: List<String>, gi: GameInterface<LakesAndMeadowsGame, LakesAndMeadowsGameMove, LakesAndMeadowsGameState>, gdi: GameDocumentInterface) : CellsGame<LakesAndMeadowsGame, LakesAndMeadowsGameMove, LakesAndMeadowsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        val dirs = arrayOf(1, 0, 3, 2)
    }

    val objArray: MutableList<LakesAndMeadowsObject>
    val lakes = mutableListOf<Position>()
    val dots: GridDots

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: LakesAndMeadowsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: LakesAndMeadowsObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        objArray = MutableList(rows * cols) { LakesAndMeadowsObject.Empty }
        dots = GridDots(rows, cols)
        for (r in 0 until rows - 1) {
            var str = layout[r]
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                if (str[c] != 'L') continue
                this[p] = LakesAndMeadowsObject.Lake
                lakes.add(p)
            }
        }
        for (r in 0 until rows - 1) {
            dots[r, 0, 2] = GridLineObject.Line
            dots[r + 1, 0, 0] = GridLineObject.Line
            dots[r, cols - 1, 2] = GridLineObject.Line
            dots[r + 1, cols - 1, 0] = GridLineObject.Line
        }
        for (c in 0 until cols - 1) {
            dots[0, c, 1] = GridLineObject.Line
            dots[0, c + 1, 3] = GridLineObject.Line
            dots[rows - 1, c, 1] = GridLineObject.Line
            dots[rows - 1, c + 1, 3] = GridLineObject.Line
        }
        val state = LakesAndMeadowsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position, dir: Int): GridLineObject = currentState[p, dir]
    fun getObject(row: Int, col: Int, dir: Int): GridLineObject = currentState[row, col, dir]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
