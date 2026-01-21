package com.zwstudio.logicpuzzlesandroid.puzzles.crossroadsx

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CrossroadsXGameState(game: CrossroadsXGame) : CellsGameState<CrossroadsXGame, CrossroadsXGameMove, CrossroadsXGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: CrossroadsXGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != CrossroadsXGame.PUZ_EMPTY || this[p] == move.obj) return GameOperationType.Invalid
        for (p2 in game.areas[game.pos2area[p]!!])
            this[p2] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: CrossroadsXGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != CrossroadsXGame.PUZ_EMPTY) return GameOperationType.Invalid
        val o = this[p]
        move.obj = if (o == 9) CrossroadsXGame.PUZ_EMPTY else o + 1
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 5/Crossroads X

        Summary
        Cross at Ten

        Description
        1. Place a number in each region from 0 to 9.
        2. When four regions borders intersect (a spot where four lines meet),
           the sum of those 4 regions must be 10.
        3. No two orthogonally adjacent regions can have the same number.
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
                    val p2 = p + CrossroadsXGame.offset[i]
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
                if (n == CrossroadsXGame.PUZ_EMPTY)
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
                    if (p1 - p2 == CrossroadsXGame.offset[0] && this[p1] <= this[p2]) {
                        isSolved = false
                        pos2state[p1] = AllowedObjectState.Error
                        pos2state[p2] = AllowedObjectState.Error
                    }
        }
    }
}