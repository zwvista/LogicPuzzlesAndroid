package com.zwstudio.logicpuzzlesandroid.puzzles.farmer

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FarmerGameState(game: FarmerGame) : CellsGameState<FarmerGame, FarmerGameMove, FarmerGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: FarmerObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: FarmerObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FarmerGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != FarmerObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FarmerGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != FarmerObject.Empty) return GameOperationType.Invalid
        move.obj = when (this[p]) {
            FarmerObject.Empty -> FarmerObject.Fv1
            FarmerObject.Fv1 -> FarmerObject.Fv2
            FarmerObject.Fv2 -> FarmerObject.Fv3
            FarmerObject.Fv3 -> FarmerObject.Empty
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 2/Farmer

        Summary
        Vegetable Gardener

        Description
        1. A Farmer has a scientific way to work his field:
        2. He plants three types of fruits or vegetables.
        3. Each area must contain either three identical plants or three different plants.
        4. When two plants are orthogonally adjacent across an area, they must be different.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        for (area in game.areas) {
            val objSet = area.map { this[it] }.toSet()
            if (objSet.contains(FarmerObject.Empty)) { isSolved = false; continue }
            val cnt = objSet.size
            // 3. Each area must contain either three identical plants or three different plants.
            if (!(cnt == 1 || cnt == 3)) {
                isSolved = false
                for (p in area)
                    pos2state[p] = AllowedObjectState.Error
            }
        }
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                val area1 = game.pos2area[p]!!
                if (o == FarmerObject.Empty) continue
                // 4. When two plants are orthogonally adjacent across an area, they must be different.
                for (os in FarmerGame.offset) {
                    val p2 = p + os
                    if (!isValid(p2)) continue
                    if (this[p2] == o && area1 != game.pos2area[p2]!!) {
                        isSolved = false
                        pos2state[p] = AllowedObjectState.Error
                    }
                }
            }
    }
}
