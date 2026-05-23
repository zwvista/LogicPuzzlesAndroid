package com.zwstudio.logicpuzzlesandroid.puzzles.landscapes

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LandscapesGameState(game: LandscapesGame) : CellsGameState<LandscapesGame, LandscapesGameMove, LandscapesGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: LandscapesObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: LandscapesObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: LandscapesGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != LandscapesObject.Empty || this[p] == move.obj) return GameOperationType.Invalid
        for (p2 in game.areas[game.pos2area[p]!!])
            this[p2] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: LandscapesGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != LandscapesObject.Empty) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            LandscapesObject.Empty -> LandscapesObject.Tree
            LandscapesObject.Tree -> LandscapesObject.Sand
            LandscapesObject.Sand -> LandscapesObject.Rock
            LandscapesObject.Rock -> LandscapesObject.Water
            LandscapesObject.Water -> LandscapesObject.Empty
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 16/Landscapes

        Summary
        Forests, Deserts, Oceans, Mountains

        Description
        1. Identify the landscape in every region, choosing between trees, sand,
           water and rocks.
        2. Two regions can't have the same landscape if they touch, not even
           diagonally.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 2. Two regions can't have the same landscape if they touch, not even
        //    diagonally.
        for ((i, indexes) in game.area2areas.withIndex()) {
            val area = game.areas[i]
            val o = this[area[0]]
            if (o == LandscapesObject.Empty) { isSolved = false; continue }
            val s = if (indexes.any {
                o == this[game.areas[it][0]]
            }) AllowedObjectState.Error else AllowedObjectState.Normal
            pos2state[area[0]] = s
            for (p in area)
                pos2state[p] = s
            if (s == AllowedObjectState.Error) isSolved = false
        }
    }
}