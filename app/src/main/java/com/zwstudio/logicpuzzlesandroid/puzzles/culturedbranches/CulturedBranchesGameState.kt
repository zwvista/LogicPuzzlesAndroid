package com.zwstudio.logicpuzzlesandroid.puzzles.culturedbranches

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CulturedBranchesGameState(game: CulturedBranchesGame) : CellsGameState<CulturedBranchesGame, CulturedBranchesGameMove, CulturedBranchesGameState>(game) {
    var objArray = Array<CulturedBranchesObject>(rows * cols) { CulturedBranchesEmptyObject }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: CulturedBranchesObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: CulturedBranchesObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = CulturedBranchesHintObject()
        updateIsSolved()
    }

    override fun setObject(move: CulturedBranchesGameMove): GameOperationType {
        if (!isValid(move.p) || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: CulturedBranchesGameMove): GameOperationType {
        val o = this[move.p]
        move.obj = if (o is CulturedBranchesEmptyObject) CulturedBranchesUpObject
            else if (o is CulturedBranchesUpObject) CulturedBranchesRightObject
            else if (o is CulturedBranchesRightObject) CulturedBranchesDownObject
            else if (o is CulturedBranchesDownObject) CulturedBranchesLeftObject
            else if (o is CulturedBranchesLeftObject) CulturedBranchesHorizontalObject
            else if (o is CulturedBranchesHorizontalObject) CulturedBranchesVerticalObject
            else if (o is CulturedBranchesVerticalObject) CulturedBranchesEmptyObject
            else o
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 3/Cultured Branches

        Summary
        Well-read trees

        Description
        1. Each Letter represents a tree. A tree has branches coming out of it,
           in any of the four directions around it.
        2. Each Letter stands for a number and no two Letters stand for the same number.
        3. The number tells you the total length of the Branches coming out of
           that Tree.
        4. In the example all 'A' means '2' and all 'B' means '4'.
        5. Every Tree having the same number must have a different number of Branches
           (1 to 4 in the possible directions around it).
        6. In the example the top Letter 'B' has 3 branches (left, right, down)
           while the bottom one has 2 (up and left).
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is CulturedBranchesEmptyObject) // 3. There can't be blank tiles.
                    isSolved = false
                else if (o is CulturedBranchesHintObject) {
                    // 1. In CulturedBranches you must fill the board with straight horizontal and
                    // vertical lines(CulturedBranches) that stem from each number.
                    // The tile with the number doesn't count.
                    val n2 = game.pos2hint[p]!!
                    var n1 = 0
                    for (i in 0 until 4) {
                        val os = CulturedBranchesGame.offset[i]
                        var p2 = p + os
                        // 3. CulturedBranches can't overlap,
                        // CulturedBranches must be in a single straight line and can't make corners.
                        while (isValid(p2)) {
                            when (this[p2]) {
                                is CulturedBranchesUpObject -> { if (i == 0) n1++; break }
                                is CulturedBranchesRightObject -> { if (i == 1) n1++; break }
                                is CulturedBranchesDownObject -> { if (i == 2) n1++; break }
                                is CulturedBranchesLeftObject -> { if (i == 3) n1++; break }
                                is CulturedBranchesHorizontalObject -> if (i % 2 == 1) n1++ else break
                                is CulturedBranchesVerticalObject -> if (i % 2 == 0) n1++ else break
                                else -> break
                            }
                            p2 += os
                        }
                    }
                    // 2. The number itself tells you how many tiles its CulturedBranches fill up.
                    val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
                    if (s != HintState.Complete) isSolved = false
                    o.state = s
                }
            }
    }
}