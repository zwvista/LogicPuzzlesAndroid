package com.zwstudio.logicpuzzlesandroid.puzzles.branches

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BranchesGameState(game: BranchesGame) : CellsGameState<BranchesGame, BranchesGameMove, BranchesGameState>(game) {
    var objArray = Array<BranchesObject>(rows * cols) { BranchesObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: BranchesObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: BranchesObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = BranchesObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: BranchesGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == BranchesObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: BranchesGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == BranchesObject.Hint) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            BranchesObject.Empty -> BranchesObject.Up
            BranchesObject.Up -> BranchesObject.Right
            BranchesObject.Right -> BranchesObject.Down
            BranchesObject.Down -> BranchesObject.Left
            BranchesObject.Left -> BranchesObject.Horizontal
            BranchesObject.Horizontal -> BranchesObject.Vertical
            BranchesObject.Vertical -> BranchesObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 2/Branches

        Summary
        Fill the board with Branches departing from the numbers

        Description
        1. In Branches you must fill the board with straight horizontal and
           vertical lines(Branches) that stem from each number.
        2. The number itself tells you how many tiles its Branches fill up.
           The tile with the number doesn't count.
        3. There can't be blank tiles and Branches can't overlap, nor run over
           the numbers. Moreover Branches must be in a single straight line
           and can't make corners.
    */
    private fun updateIsSolved() {
        isSolved = true
        pos2state.clear()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == BranchesObject.Empty) // 3. There can't be blank tiles.
                    isSolved = false
                else if (o == BranchesObject.Hint) {
                    // 1. In Branches you must fill the board with straight horizontal and
                    // vertical lines(Branches) that stem from each number.
                    // The tile with the number doesn't count.
                    val n2 = game.pos2hint[p]!!
                    var n1 = 0
                    for (i in 0 until 4) {
                        val os = BranchesGame.offset[i]
                        var p2 = p + os
                        // 3. Branches can't overlap,
                        // Branches must be in a single straight line and can't make corners.
                        while (isValid(p2)) {
                            when (this[p2]) {
                                BranchesObject.Up -> { if (i == 0) n1++; break }
                                BranchesObject.Right -> { if (i == 1) n1++; break }
                                BranchesObject.Down -> { if (i == 2) n1++; break }
                                BranchesObject.Left -> { if (i == 3) n1++; break }
                                BranchesObject.Horizontal -> if (i % 2 == 1) n1++ else break
                                BranchesObject.Vertical -> if (i % 2 == 0) n1++ else break
                                else -> break
                            }
                            p2 += os
                        }
                    }
                    // 2. The number itself tells you how many tiles its Branches fill up.
                    val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
                    if (s != HintState.Complete) isSolved = false
                    pos2state[p] = s
                }
            }
    }
}