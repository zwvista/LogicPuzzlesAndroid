package com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class SumscrapersGameState(game: SumscrapersGame) : CellsGameState<SumscrapersGame, SumscrapersGameMove, SumscrapersGameState>(game) {
    val objArray = game.objArray.copyOf()
    var row2state = Array(rows * 2) { HintState.Normal }
    var col2state = Array(cols * 2) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}
    fun getState(row: Int, col: Int) = when {
        row == 0 && col >= 1 && col < cols - 1 -> col2state[col * 2]
        row == rows - 1 && col >= 1 && col < cols - 1 -> col2state[col * 2 + 1]
        col == 0 && row >= 1 && row < rows - 1 -> row2state[row * 2]
        col == cols - 1 && row >= 1 && row < rows - 1 -> row2state[row * 2 + 1]
        else -> HintState.Normal
    }

    init {
        updateIsSolved()
    }

    fun setObject(move: SumscrapersGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: SumscrapersGameMove): Boolean {
        val p = move.p
        if (!isValid(p)) return false
        val o = this[p]
        move.obj = (o + 1) % (game.intMax() + 1)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 6/Sumscrapers

        Summary
        Sum the skyline!

        Description
        1. The grid in the center represents a city from above. Each cell contain
           a skyscraper, of different height.
        2. The goal is to guess the height of each Skyscraper.
        3. Each row and column can't have two Skyscrapers of the same height.
        4. The numbers on the boarders tell the SUM of the heights of the Skyscrapers
           you see from there, keeping mind that a higher skyscraper hides a lower one.
           Skyscrapers are numbered from 1 (lowest) to the grid size (highest).
        5. Each row and column can't have similar Skyscrapers.
    */
    private fun updateIsSolved() {
        isSolved = true
        val numss = mutableListOf<List<Int>>()
        val nums = mutableListOf<Int>()
        for (r in 1 until rows - 1) {
            val h1 = this[r, 0]
            val h2 = this[r, cols - 1]
            var n1 = 0
            var n2 = 0
            var n11 = 0
            var n21 = 0
            nums.clear()
            for (c in 1 until cols - 1) {
                val n12 = this[r, c]
                val n22 = this[r, cols - 1 - c]
                if (n11 < n12) {
                    n11 = n12
                    n1 += n12
                }
                if (n21 < n22) {
                    n21 = n22
                    n2 += n22
                }
                if (n12 == 0) continue
                if (nums.contains(n12))
                    isSolved = false
                else
                    nums.add(n12)
            }
            // 4. The numbers on the boarders tell you the SUM of the heights skyscrapers
            // you see from there, keeping mind that a higher skyscraper hides a lower one.
            // Skyscrapers are numbered from 1(lowest) to the grid size(highest).
            val s1 = if (n1 == 0) HintState.Normal else if (n1 == h1) HintState.Complete else HintState.Error
            val s2 = if (n2 == 0) HintState.Normal else if (n2 == h2) HintState.Complete else HintState.Error
            row2state[r * 2] = s1
            row2state[r * 2 + 1] = s2
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false
            if (nums.size != game.intMax()) isSolved = false
            // 5. Each row and column can't have similar Skyscrapers.
            if (numss.contains(nums))
                isSolved = false
            else
                numss.add(nums)
        }
        for (c in 1 until cols - 1) {
            val h1 = this[0, c]
            val h2 = this[rows - 1, c]
            var n1 = 0
            var n2 = 0
            var n11 = 0
            var n21 = 0
            nums.clear()
            for (r in 1 until rows - 1) {
                val n12 = this[r, c]
                val n22 = this[rows - 1 - r, c]
                if (n11 < n12) {
                    n11 = n12
                    n1 += n12
                }
                if (n21 < n22) {
                    n21 = n22
                    n2 += n22
                }
                if (n12 == 0) continue
                if (nums.contains(n12))
                    isSolved = false
                else
                    nums.add(n12)
            }
            // 4. The numbers on the boarders tell you the SUM of the heights skyscrapers
            // you see from there, keeping mind that a higher skyscraper hides a lower one.
            // Skyscrapers are numbered from 1(lowest) to the grid size(highest).
            val s1 = if (n1 == 0) HintState.Normal else if (n1 == h1) HintState.Complete else HintState.Error
            val s2 = if (n2 == 0) HintState.Normal else if (n2 == h2) HintState.Complete else HintState.Error
            col2state[c * 2] = s1
            col2state[c * 2 + 1] = s2
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false
            if (nums.size != game.intMax()) isSolved = false
            // 5. Each row and column can't have similar Skyscrapers.
            if (numss.contains(nums))
                isSolved = false
            else
                numss.add(nums)
        }
    }
}