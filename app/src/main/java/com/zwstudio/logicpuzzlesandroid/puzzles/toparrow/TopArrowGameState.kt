package com.zwstudio.logicpuzzlesandroid.puzzles.toparrow

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TopArrowGameState(game: TopArrowGame) : CellsGameState<TopArrowGame, TopArrowGameMove, TopArrowGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: TopArrowGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != TopArrowGame.PUZ_EMPTY || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TopArrowGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != TopArrowGame.PUZ_EMPTY) return GameOperationType.Invalid
        val o = this[p]
        move.obj = if (o == game.areas[game.pos2area[p]!!].size) TopArrowGame.PUZ_EMPTY else o + 1
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 3/Top Arrow

        Summary
        Top numbers

        Description
        1. Fill each area with each of the digits from 1 to the size of the
           area itself.
        2. Arrows point to the biggest number around it in the four directions
           (up, left, down, right).
        3. When two numbers are orthogonally adjacent across areas, the numbers
           must be different.
        4. There can't be ties for the biggest number in the four directions.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        // 2. Two orthogonally adjacent numbers must be different.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                for (i in listOf(1, 2)) {
                    val p2 = p + TopArrowGame.offset[i]
                    if (!(isValid(p2) && this[p] == this[p2])) continue
                    isSolved = false
                    pos2state[p] = AllowedObjectState.Error
                    pos2state[p2] = AllowedObjectState.Error
                }
            }
        for (area in game.areas) {
            val num2rng = mutableMapOf<Int, MutableList<Position>>()
            for (p in area) {
                val n = this[p]
                if (n == TopArrowGame.PUZ_EMPTY)
                    isSolved = false
                else
                    num2rng.getOrPut(n) { mutableListOf() }.add(p)
            }
            // 1. Fill each area with every number ranging from 1 to the size of the area.
            for ((_, rng) in num2rng)
                if (rng.size > 1) {
                    isSolved = false
                    for (p in rng)
                        pos2state[p] = AllowedObjectState.Error
                }
            // 3. In one area, if a number is right above another, the upper one must be
            //    higher than the lower one. This only applies to numbers on top of each
            //    other in the same area.
            for (p1 in area)
                for (p2 in area)
                    if (p1 - p2 == TopArrowGame.offset[0] && this[p1] <= this[p2]) {
                        isSolved = false
                        pos2state[p1] = AllowedObjectState.Error
                        pos2state[p2] = AllowedObjectState.Error
                    }
        }
    }
}