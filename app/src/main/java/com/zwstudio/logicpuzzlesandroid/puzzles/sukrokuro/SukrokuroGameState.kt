package com.zwstudio.logicpuzzlesandroid.puzzles.sukrokuro

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import kotlin.math.abs

class SukrokuroGameState(game: SukrokuroGame) : CellsGameState<SukrokuroGame, SukrokuroGameMove, SukrokuroGameState>(game) {
    val pos2num = LinkedHashMap(game.pos2num)
    val pos2horzHint = mutableMapOf<Position, HintState>()
    val pos2vertHint = mutableMapOf<Position, HintState>()
    val dotsVertState = mutableMapOf<Position, HintState>()
    val dotsHorzState = mutableMapOf<Position, HintState>()

    operator fun get(p: Position) = pos2num[p]
    operator fun set(p: Position, obj: Int) {pos2num[p] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: SukrokuroGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == null || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: SukrokuroGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == null) return GameOperationType.Invalid
        val o = this[p]!!
        move.obj = (o + 1) % 10
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 4/Sukrokuro

        Summary
        All mixed up!

        Description
        1. Sukrokuro combines Sudoku, Kropki and Kakuro.
        2. Fill in the white cells, one number in each, so that each column and row
           contains the nubmers 1 through 7 exactly once.
        3. Black cells contain clues, which tell you the sum of the number in
           consecutive cells at its right and downward.
        4. A dot separated tiles where the absolute difference between the numbers
           is 1.
        5. If a dot is absent between two cells, the difference between the numbers
           must be more than 1.
    */
    private fun updateIsSolved() {
        isSolved = true
        for ((p, n2) in game.pos2horzHint) {
            val os = SukrokuroGame.offset[1]
            var p2 = p + os
            var n1 = 0
            var lastN = 0
            while (true) {
                val n = pos2num[p2] ?: break
                n1 += n
                p2 += os
                // 3. You can write numbers 1 to 9 in the tiles, however no same number should
                // appear in a consecutive row.
                if (n == lastN) {
                    isSolved = false
                    pos2horzHint[p2] = HintState.Error
                    pos2horzHint[p2 - os] = HintState.Error
                }
                lastN = n
            }
            // 2. The number on at the left of a row gives you
            // the sum of the numbers in that row.
            val s = if (n1 == 0) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2horzHint[p] = s
            if (s != HintState.Complete) isSolved = false
        }
        for ((p, n2) in game.pos2vertHint) {
            val os = SukrokuroGame.offset[2]
            var p2 = p + os
            var n1 = 0
            var lastN = 0
            while (true) {
                val n = pos2num[p2] ?: break
                n1 += n
                p2 += os
                // 3. You can write numbers 1 to 9 in the tiles, however no same number should
                // appear in a consecutive column.
                if (n == lastN) {
                    isSolved = false
                    pos2vertHint[p2] = HintState.Error
                    pos2vertHint[p2 - os] = HintState.Error
                }
                lastN = n
            }
            // 2. The number on the top of a column gives you
            // the sum of the numbers in that column.
            val s = if (n1 == 0) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2vertHint[p] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (p in game.dotsVert) {
            val (n1, n2) = pos2num[p]!! to pos2num[p + Position.South]!!
            val s = if (n1 == 0 || n2 == 0) HintState.Normal else if (abs(n1 - n2) == 1) HintState.Complete else HintState.Error
            dotsVertState[p] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (p in game.dotsHorz) {
            val (n1, n2) = pos2num[p]!! to pos2num[p + Position.East]!!
            val s = if (n1 == 0 || n2 == 0) HintState.Normal else if (abs(n1 - n2) == 1) HintState.Complete else HintState.Error
            dotsHorzState[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}