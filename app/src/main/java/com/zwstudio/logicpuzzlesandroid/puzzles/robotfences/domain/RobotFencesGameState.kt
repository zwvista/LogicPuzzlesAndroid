package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class RobotFencesGameState(game: RobotFencesGame) : CellsGameState<RobotFencesGame, RobotFencesGameMove, RobotFencesGameState>(game) {
    var objArray = game.objArray.copyOf()
    var row2state = Array(rows()) { HintState.Normal }
    var col2state = Array(cols()) { HintState.Normal }
    var area2state = Array(game.areas.size) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: RobotFencesGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != 0 || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: RobotFencesGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != 0) return false
        val o = this[p]
        move.obj = (o + 1) % (cols() + 1)
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
            val nums2 = nums.toSet().toList()
            // 2. Numbers can only be in range 1 to N where N is the board size.
            val s: HintState = if (nums2[0] == 0) HintState.Normal else if (nums2.size == nums.size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            return s
        }
        // 3. No same number can appear in the same row.
        for (r in 0 until rows())
            row2state[r] = f((0 until cols()).map { this[r, it] })
        // 3. No same number can appear in the same column.
        for (c in 0 until cols())
            col2state[c] = f((0 until rows()).map { this[it, c] })
        // 1. You need to fill each region with a randomly ordered sequence of numbers.
        for (i in 0 until game.areas.size)
            area2state[i] = f(game.areas[i].map { this[it] })
    }
}