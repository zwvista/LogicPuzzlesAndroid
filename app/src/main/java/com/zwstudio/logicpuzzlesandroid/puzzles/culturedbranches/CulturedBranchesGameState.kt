package com.zwstudio.logicpuzzlesandroid.puzzles.culturedbranches

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CulturedBranchesGameState(game: CulturedBranchesGame) : CellsGameState<CulturedBranchesGame, CulturedBranchesGameMove, CulturedBranchesGameState>(game) {
    var objArray = Array<CulturedBranchesObject>(rows * cols) { CulturedBranchesObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: CulturedBranchesObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: CulturedBranchesObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = CulturedBranchesObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: CulturedBranchesGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == CulturedBranchesObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: CulturedBranchesGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == CulturedBranchesObject.Hint) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            CulturedBranchesObject.Empty -> CulturedBranchesObject.Up
            CulturedBranchesObject.Up -> CulturedBranchesObject.Right
            CulturedBranchesObject.Right -> CulturedBranchesObject.Down
            CulturedBranchesObject.Down -> CulturedBranchesObject.Left
            CulturedBranchesObject.Left -> CulturedBranchesObject.Horizontal
            CulturedBranchesObject.Horizontal -> CulturedBranchesObject.Vertical
            CulturedBranchesObject.Vertical -> CulturedBranchesObject.Empty
            else -> o
        }
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
        isSolved = true
        val ch2rng = mutableMapOf<Char, MutableList<Position>>()
        val ch2lens = mutableMapOf<Char, MutableSet<Int>>()
        val ch2nums = mutableMapOf<Char, MutableSet<Int>>()
        pos2state.clear()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == CulturedBranchesObject.Empty) // 3. There can't be blank tiles.
                    isSolved = false
                else if (o == CulturedBranchesObject.Hint) {
                    // 1. In CulturedBranches you must fill the board with straight horizontal and
                    // vertical lines(CulturedBranches) that stem from each number.
                    // The tile with the number doesn't count.
                    val ch = game.pos2hint[p]!!
                    var len = 0
                    var num = 0
                    for (i in 0 until 4) {
                        val os = CulturedBranchesGame.offset[i]
                        var p2 = p + os
                        var n = 0
                        // 3. CulturedBranches can't overlap,
                        // CulturedBranches must be in a single straight line and can't make corners.
                        while (isValid(p2)) {
                            when (this[p2]) {
                                CulturedBranchesObject.Up -> { if (i == 0) n++; break }
                                CulturedBranchesObject.Right -> { if (i == 1) n++; break }
                                CulturedBranchesObject.Down -> { if (i == 2) n++; break }
                                CulturedBranchesObject.Left -> { if (i == 3) n++; break }
                                CulturedBranchesObject.Horizontal -> if (i % 2 == 1) n++ else break
                                CulturedBranchesObject.Vertical -> if (i % 2 == 0) n++ else break
                                else -> break
                            }
                            p2 += os
                        }
                        if (n > 0) {
                            len += n
                            num += 1
                        }
                    }
                    ch2rng.getOrPut(ch) { mutableListOf() }.add(p)
                    ch2lens.getOrPut(ch) { mutableSetOf() }.add(len)
                    ch2nums.getOrPut(ch) { mutableSetOf() }.add(num)
                }
            }
        // 2. Each Letter stands for a number and no two Letters stand for the same number.
        // 3. The number tells you the total length of the Branches coming out of
        //    that Tree.
        // 5. Every Tree having the same number must have a different number of Branches
        //    (1 to 4 in the possible directions around it).
        for ((ch, rng) in ch2rng) {
            val lens = ch2lens[ch]!!
            val nums = ch2nums[ch]!!
            val s = if (lens.all { it == 0 }) HintState.Normal else if (lens.size == 1 && nums.size == rng.size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            for (p in rng) pos2state[p] = s
        }
    }
}