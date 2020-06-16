package com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TheOddBrickGameState(game: TheOddBrickGame) : CellsGameState<TheOddBrickGame, TheOddBrickGameMove, TheOddBrickGameState>(game) {
    var objArray = game.objArray.copyOf()
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }
    var area2state = Array(game.areas.size) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: TheOddBrickGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != 0 || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    override fun switchObject(move: TheOddBrickGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != 0) return false
        val o = this[p]
        move.obj = (o + 1) % (cols + 1)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 14/The Odd Brick

        Summary
        Even Bricks are strange, sometimes

        Description
        1. On the board there is a wall, made of 2*1 and 1*1 bricks.
        2. Each 2*1 brick contains and odd and an even number, while 1*1 bricks
           can contain any number.
        3. Each row and column contains numbers 1 to N, where N is the side of
           the board.
    */
    private fun updateIsSolved() {
        isSolved = true
        fun f(nums: List<Int>): HintState {
            val nums2 = nums.toSet().toList()
            // 3. Each row and column contains numbers 1 to N, where N is the side of
            // the board.
            val s = if (nums2[0] == 0) HintState.Normal else if (nums2.size == nums.size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            return s
        }
        for (r in 0 until rows)
            row2state[r] = f((0 until cols).map { this[r, it] })
        for (c in 0 until cols)
            col2state[c] = f((0 until rows).map { this[it, c] })
        for (i in 0 until game.areas.size) {
            val nums = game.areas[i].map { this[it] }
            // 2. Each 2*1 brick contains and odd and an even number, while 1*1 bricks
            // can contain any number.
            area2state[i] = if (nums.contains(0)) HintState.Normal else if (nums.size == 1 || nums[0] % 2 == 0 && nums[1] % 2 == 1 || nums[0] % 2 == 1 && nums[1] % 2 == 0) HintState.Complete else HintState.Error
            if (area2state[i] != HintState.Complete) isSolved = false
        }
    }
}