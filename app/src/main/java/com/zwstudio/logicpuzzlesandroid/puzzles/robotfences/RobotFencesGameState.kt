package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class RobotFencesGameState(game: RobotFencesGame) : CellsGameState<RobotFencesGame, RobotFencesGameMove, RobotFencesGameState>(game) {
    val objArray = game.objArray.copyOf()
    val row2state = Array(rows) { HintState.Normal }
    val col2state = Array(cols) { HintState.Normal }
    val area2state = Array(game.areas.size) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: RobotFencesGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != 0 || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: RobotFencesGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != 0) return GameOperationType.Invalid
        val o = this[p]
        move.obj = (o + 1) % (cols + 1)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/Robot Fences

        Summary
        BZZZZliip ...cows?

        Description
        1. A bit like Robot Crosswords, you need to fill each region with a
           randomly ordered sequence of numbers.
        2. Numbers can only be in range 1 to N where N is the board size.
        3. No same number can appear in the same row or column.
    */
    private fun updateIsSolved() {
        isSolved = true
        fun f(nums: List<Int>): HintState {
            val nums2 = nums.toSet().toList().sorted()
            // 2. Numbers can only be in range 1 to N where N is the board size.
            val s = if (nums2[0] == 0) HintState.Normal else if (nums2.size == nums.size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            return s
        }
        // 3. No same number can appear in the same row.
        for (r in 0..<rows)
            row2state[r] = f((0..<cols).map { this[r, it] })
        // 3. No same number can appear in the same column.
        for (c in 0..<cols)
            col2state[c] = f((0..<rows).map { this[it, c] })
        // 1. You need to fill each region with a randomly ordered sequence of numbers.
        for (i in 0..<game.areas.size)
            area2state[i] = f(game.areas[i].map { this[it] })
    }
}