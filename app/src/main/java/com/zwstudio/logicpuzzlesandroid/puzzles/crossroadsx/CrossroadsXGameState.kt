package com.zwstudio.logicpuzzlesandroid.puzzles.crossroadsx

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CrossroadsXGameState(game: CrossroadsXGame) : CellsGameState<CrossroadsXGame, CrossroadsXGameMove, CrossroadsXGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, AllowedObjectState>()
    var invalidCrossroads = listOf<Position>()

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
        // 2. When four regions borders intersect (a spot where four lines meet),
        //    the sum of those 4 regions must be 10.
        invalidCrossroads = game.crossroads.filter { p ->
            val rng = CrossroadsXGame.offset3.map { p + it }
            !(rng.all { this[it] != CrossroadsXGame.PUZ_EMPTY } &&
                    rng.fold(0) { acc, p2 -> acc + this[p2] } == game.sum)
        }
        if (invalidCrossroads.isNotEmpty()) isSolved = false
        // 3. No two orthogonally adjacent regions can have the same number.
        for ((i, area) in game.areas.withIndex()) {
            val n = this[area[0]]
            val areas = game.area2areas[i]
                .map { this.game.areas[it] }
                .filter { this[it[0]] == n }
            if (areas.isEmpty()) {
                for (p in area) pos2state[p] = AllowedObjectState.Normal
            } else {
                isSolved = false
                for (area2 in listOf(area) + areas)
                    for (p in area2) pos2state[p] = AllowedObjectState.Error
            }
        }
    }
}