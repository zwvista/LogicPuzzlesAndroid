package com.zwstudio.logicpuzzlesandroid.puzzles.zengardens

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ZenGardensGameState(game: ZenGardensGame) : CellsGameState<ZenGardensGame, ZenGardensGameMove, ZenGardensGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: ZenGardensGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != ' ' || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ZenGardensGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[move.p] != ' ') return GameOperationType.Invalid
        val o = this[p]
        move.obj = if (o == ' ') '1' else if (o == '3') ' ' else (o.code + 1).toChar()
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
        for (r in 0 until rows)
            for (c in 0 until cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        // 4. The teaching says that any three contiguous tiles vertically,
        //    horizontally or diagonally must NOT be:
        //    -> all different
        //    -> all equal
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                for (i in 2 .. 4) {
                    val os = ZenGardensGame.offset[i]
                    val tiles = mutableListOf(p)
                    var p2 = p + os
                    for (j in 1 until 3) {
                        if (!isValid(p2)) break
                        tiles.add(p2)
                        p2 += os
                    }
                    if (tiles.size < 3) continue
                    val chSet = tiles.map { this[it] }.toSet()
                    if (chSet.contains(' ')) {
                        isSolved = false
                        continue
                    }
                    if (chSet.size != 2) {
                        isSolved = false
                        for (p2 in tiles)
                            pos2state[p2] = AllowedObjectState.Error
                    }
                }
            }
    }
}