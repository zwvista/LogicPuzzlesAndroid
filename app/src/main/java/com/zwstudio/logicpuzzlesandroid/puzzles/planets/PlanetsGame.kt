package com.zwstudio.logicpuzzlesandroid.puzzles.planets

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PlanetsGame(layout: List<String>, gi: GameInterface<PlanetsGame, PlanetsGameMove, PlanetsGameState>, gdi: GameDocumentInterface) : CellsGame<PlanetsGame, PlanetsGameMove, PlanetsGameState>(gi, gdi) {
    companion object {
        val offset = Position.Directions4
        val chars = " 01392846C"
        val isLitMap: Map<List<Int>, PlanetsObject> = mapOf(
            listOf<Int>() to PlanetsObject.None,
            listOf(0) to PlanetsObject.North,
            listOf(1) to PlanetsObject.East,
            listOf(2) to PlanetsObject.South,
            listOf(3) to PlanetsObject.West,
            listOf(0, 1) to PlanetsObject.NorthEast,
            listOf(0, 3) to PlanetsObject.NorthWest,
            listOf(1, 2) to PlanetsObject.SouthEast,
            listOf(2, 3) to PlanetsObject.SouthWest,
        )
    }

    val objArray: Array<PlanetsObject>
    val planets = mutableListOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: PlanetsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: PlanetsObject) {this[p.row, p.col] = obj}

    init {
        size = Position(layout.size, layout[0].length)
        objArray = Array(rows * cols) { PlanetsObject.Empty }
        for (r in 0 until rows) {
            val str = layout[r]
            for (c in 0 until cols) {
                val ch = str[c]
                val p = Position(r, c)
                val n = chars.indexOf(ch)
                this[p] = PlanetsObject.entries[n]
                if (ch != ' ')
                    planets.add(p)
            }
        }
        val state = PlanetsGameState(this)
        levelInitialized(state)
    }

    fun getObject(p: Position) = currentState[p]
    fun getObject(row: Int, col: Int) = currentState[row, col]
    fun pos2state(p: Position) = currentState.pos2state[p]
}
