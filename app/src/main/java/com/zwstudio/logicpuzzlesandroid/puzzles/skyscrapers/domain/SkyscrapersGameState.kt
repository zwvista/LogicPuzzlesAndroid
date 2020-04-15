package com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import java.util.*

class SkyscrapersGameState(game: SkyscrapersGame) : CellsGameState<SkyscrapersGame?, SkyscrapersGameMove?, SkyscrapersGameState?>(game) {
    private val objArray: IntArray
    var row2state: Array<HintState?>
    var col2state: Array<HintState?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: Int) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Int) {
        set(p!!.row, p.col, obj)
    }

    fun getState(row: Int, col: Int) = if (row == 0 && col >= 1 && col < cols() - 1) col2state[col * 2] else if (row == rows() - 1 && col >= 1 && col < cols() - 1) col2state[col * 2 + 1] else if (col == 0 && row >= 1 && row < rows() - 1) row2state[row * 2] else if (col == cols() - 1 && row >= 1 && row < rows() - 1) row2state[row * 2 + 1] else HintState.Normal

    fun setObject(move: SkyscrapersGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || get(p) == move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: SkyscrapersGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p)) return false
        val o = get(p)
        move.obj = (o + 1) % (game.intMax() + 1)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/Skyscrapers

        Summary
        Guess skyscrapers heights, judging from the skyline

        Description
        1. The grid in the center represents a city from above. Each cell contain
           a skyscraper, of different height.
        2. The goal is to guess the height of each Skyscraper.
        3. Each row and column can't have two Skyscrapers of the same height.
        4. The numbers on the boarders tell you how many skyscrapers you see from
           there, keeping mind that a higher skyscraper hides a lower one.
           Skyscrapers are numbered from 1(lowest) to the grid size(highest).
        5. Each row and column can't have similar Skyscrapers.
    */
    private fun updateIsSolved() {
        isSolved = true
        val numss = mutableListOf<List<Int>>()
        val nums = mutableListOf<Int>()
        for (r in 1 until rows() - 1) {
            val h1 = get(r, 0)
            val h2 = get(r, cols() - 1)
            var n1 = 0
            var n2 = 0
            var n11 = 0
            var n21 = 0
            nums.clear()
            for (c in 1 until cols() - 1) {
                val n12 = get(r, c)
                val n22 = get(r, cols() - 1 - c)
                if (n11 < n12) {
                    n11 = n12
                    n1++
                }
                if (n21 < n22) {
                    n21 = n22
                    n2++
                }
                if (n12 == 0) continue
                // 2. Each row can't have two Skyscrapers of the same height.
                if (nums.contains(n12)) isSolved = false else nums.add(n12)
            }
            // 4. The numbers on the boarders tell you how many skyscrapers you see from
            // there, keeping mind that a higher skyscraper hides a lower one.
            // Skyscrapers are numbered from 1(lowest) to the grid size(highest).
            val s1: HintState = if (n1 == 0) HintState.Normal else if (n1 == h1) HintState.Complete else HintState.Error
            val s2: HintState = if (n2 == 0) HintState.Normal else if (n2 == h2) HintState.Complete else HintState.Error
            row2state[r * 2] = s1
            row2state[r * 2 + 1] = s2
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false
            if (nums.size != game.intMax()) isSolved = false
            // 5. Each row and column can't have similar Skyscrapers.
            if (numss.contains(nums)) isSolved = false else numss.add(nums)
        }
        for (c in 1 until cols() - 1) {
            val h1 = get(0, c)
            val h2 = get(rows() - 1, c)
            var n1 = 0
            var n2 = 0
            var n11 = 0
            var n21 = 0
            nums.clear()
            for (r in 1 until rows() - 1) {
                val n12 = get(r, c)
                val n22 = get(rows() - 1 - r, c)
                if (n11 < n12) {
                    n11 = n12
                    n1++
                }
                if (n21 < n22) {
                    n21 = n22
                    n2++
                }
                if (n12 == 0) continue
                // 2. Each column can't have two Skyscrapers of the same height.
                if (nums.contains(n12)) isSolved = false else nums.add(n12)
            }
            // 4. The numbers on the boarders tell you how many skyscrapers you see from
            // there, keeping mind that a higher skyscraper hides a lower one.
            // Skyscrapers are numbered from 1(lowest) to the grid size(highest).
            val s1: HintState = if (n1 == 0) HintState.Normal else if (n1 == h1) HintState.Complete else HintState.Error
            val s2: HintState = if (n2 == 0) HintState.Normal else if (n2 == h2) HintState.Complete else HintState.Error
            col2state[c * 2] = s1
            col2state[c * 2 + 1] = s2
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false
            if (nums.size != game.intMax()) isSolved = false
            // 5. Each row and column can't have similar Skyscrapers.
            if (numss.contains(nums)) isSolved = false else numss.add(nums)
        }
    }

    init {
        objArray = IntArray(rows() * cols())
        for (r in 0 until rows()) for (c in 0 until cols()) set(r, c, game.get(r, c))
        row2state = arrayOfNulls<HintState>(rows() * 2)
        col2state = arrayOfNulls<HintState>(cols() * 2)
        updateIsSolved()
    }
}