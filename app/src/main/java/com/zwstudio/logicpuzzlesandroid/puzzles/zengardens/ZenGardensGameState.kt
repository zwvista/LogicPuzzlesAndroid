package com.zwstudio.logicpuzzlesandroid.puzzles.zengardens

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ZenGardensGameState(game: ZenGardensGame) : CellsGameState<ZenGardensGame, ZenGardensGameMove, ZenGardensGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: ZenGardensObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: ZenGardensObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: ZenGardensGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ZenGardensObject.Stone || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ZenGardensGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ZenGardensObject.Stone) return GameOperationType.Invalid
        val o = this[p]
        move.obj = if (o == ZenGardensObject.Stone) ZenGardensObject.Leaf else ZenGardensObject.Stone
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 3/Zen Gardens

        Summary
        Many Zen Masters

        Description
        1. Put a leaf on every Zen Garden (area).
        2. A Leaf can only be on a Rock.
        3. Three Rocks in a row (horizontally, vertically or diagonally) can't
           have all the leaves or no leaves.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        // 1. Put a leaf on every Zen Garden (area).
        for (area in game.areas) {
            val rng = area.filter { this[it] == ZenGardensObject.Leaf }
        if (rng.size != 1) {
                isSolved = false
                for (p in rng)
                    pos2state[p] = AllowedObjectState.Error
            }
        }
        // 3. Three Rocks in a row (horizontally, vertically or diagonally) can't
        //    have all the leaves or no leaves.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                for (i in 2 .. 4) {
                    val os = ZenGardensGame.offset3[i]
                    val tiles = mutableListOf(p)
                    var p2 = p + os
                    for (j in 1..<3) {
                        if (!isValid(p2)) break
                        tiles.add(p2)
                        p2 += os
                    }
                    if (tiles.size < 3) continue
                    val objSet = tiles.map { this[it] }.toSet()
                    if (objSet.contains(ZenGardensObject.Empty)) continue
                    if (objSet.size != 2) {
                        isSolved = false
                        for (p2 in tiles)
                            pos2state[p2] = AllowedObjectState.Error
                    }
                }
            }
    }
}