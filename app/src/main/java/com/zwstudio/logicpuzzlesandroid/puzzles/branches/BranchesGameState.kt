package com.zwstudio.logicpuzzlesandroid.puzzles.branches

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameChangeType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BranchesGameState(game: BranchesGame) : CellsGameState<BranchesGame, BranchesGameMove, BranchesGameState>(game) {
    var objArray = Array<BranchesObject>(rows * cols) { BranchesEmptyObject }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: BranchesObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: BranchesObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = BranchesHintObject()
        updateIsSolved()
    }

    override fun setObject(move: BranchesGameMove): GameChangeType {
        if (!isValid(move.p) || this[move.p] == move.obj) return GameChangeType.None
        this[move.p] = move.obj
        updateIsSolved()
        return GameChangeType.Level
    }

    override fun switchObject(move: BranchesGameMove): GameChangeType {
        val o = this[move.p]
        move.obj = if (o is BranchesEmptyObject) BranchesUpObject
            else if (o is BranchesUpObject) BranchesRightObject
            else if (o is BranchesRightObject) BranchesDownObject
            else if (o is BranchesDownObject) BranchesLeftObject
            else if (o is BranchesLeftObject) BranchesHorizontalObject
            else if (o is BranchesHorizontalObject) BranchesVerticalObject
            else if (o is BranchesVerticalObject) BranchesEmptyObject
            else o
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
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is BranchesEmptyObject) // 3. There can't be blank tiles.
                    isSolved = false
                else if (o is BranchesHintObject) {
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
                                is BranchesUpObject -> { if (i == 0) n1++; break }
                                is BranchesRightObject -> { if (i == 1) n1++; break }
                                is BranchesDownObject -> { if (i == 2) n1++; break }
                                is BranchesLeftObject -> { if (i == 3) n1++; break }
                                is BranchesHorizontalObject -> if (i % 2 == 1) n1++ else break
                                is BranchesVerticalObject -> if (i % 2 == 0) n1++ else break
                                else -> break
                            }
                            p2 += os
                        }
                    }
                    // 2. The number itself tells you how many tiles its Branches fill up.
                    val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
                    if (s != HintState.Complete) isSolved = false
                    o.state = s
                }
            }
    }
}