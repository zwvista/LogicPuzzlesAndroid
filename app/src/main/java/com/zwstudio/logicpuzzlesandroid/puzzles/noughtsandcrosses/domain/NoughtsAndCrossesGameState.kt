package com.zwstudio.logicpuzzlesandroid.puzzles.noughtsandcrosses.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class NoughtsAndCrossesGameState(game: NoughtsAndCrossesGame) : CellsGameState<NoughtsAndCrossesGame, NoughtsAndCrossesGameMove, NoughtsAndCrossesGameState>(game) {
    var objArray = game.objArray.copyOf()
    var row2state = Array(rows()) { HintState.Normal }
    var col2state = Array(cols()) { HintState.Normal }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: NoughtsAndCrossesGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != ' ' || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: NoughtsAndCrossesGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values()[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p) || game[p] != ' ') return false
        val o = this[p]
        move.obj = when (o) {
            ' ' -> if (markerOption == MarkerOptions.MarkerFirst) '.' else '1'
            '.' -> if (markerOption == MarkerOptions.MarkerFirst) '1' else ' '
            game.chMax -> if (markerOption == MarkerOptions.MarkerLast) '.' else ' '
            else -> o + 1
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 16/Noughts & Crosses

        Summary
        Spot the Number

        Description
        1. Place all numbers from 1 to N on each row and column - just once,
           without repeating.
        2. In other words, all numbers must appear just once on each row and column.
        3. A circle marks where a number must go.
        4. A cross marks where no number can go.
        5. All other cells can contain a number or be empty.
    */
    private fun updateIsSolved() {
        isSolved = true
        fun f(nums: List<Char>): HintState {
            // 4. A cross marks where no number can go.
            // 5. All other cells can contain a number or be empty.
            val nums2 = nums.filter { !" .X".contains(it) }
            // 2. All numbers must appear just once.
            val s = if (nums2.size == game.chMax - '0' && nums2.size == nums2.toSet().size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            return s
        }
        // 2. All numbers must appear just once on each row.
        for (r in 0 until rows())
            row2state[r] = f((0 until cols()).map { this[r, it] })
        // 2. All numbers must appear just once on each column.
        for (c in 0 until cols())
            col2state[c] = f((0 until rows()).map{ this[it, c] })
        // 3. A circle marks where a number must go.
        for (p in game.noughts) {
            val ch = this[p]
            val s = if (ch == ' ' || ch == '.') HintState.Normal else HintState.Complete
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}