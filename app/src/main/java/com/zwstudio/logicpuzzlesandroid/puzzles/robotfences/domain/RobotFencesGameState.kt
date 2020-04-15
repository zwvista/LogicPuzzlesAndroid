package com.zwstudio.logicpuzzlesandroid.puzzles.robotfences.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.data.Set
import fj.data.Stream

class RobotFencesGameState(game: RobotFencesGame) : CellsGameState<RobotFencesGame?, RobotFencesGameMove?, RobotFencesGameState?>(game) {
    var objArray: IntArray
    var row2state: Array<HintState?>
    var col2state: Array<HintState?>
    var area2state: Array<HintState?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: Int) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Int) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: RobotFencesGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) != 0 || get(p) == move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: RobotFencesGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) != 0) return false
        val o = get(p)
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
        val f: F<List<Int>, HintState> = F<List<Int>, HintState> { nums: List<Int> ->
            val nums2 = Set.iterableSet(Ord.intOrd, nums).toJavaList()
            // 2. Numbers can only be in range 1 to N where N is the board size.
            val s: HintState = if (nums2[0] == 0) HintState.Normal else if (nums2.size == nums.size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            s
        }
        // 3. No same number can appear in the same row.
        Stream.range(0, rows().toLong()).foreachDoEffect(Effect1<Int> { r: Int -> row2state[r] = f.f(Stream.range(0, cols().toLong()).map<Int>(F<Int, Int> { c: Int -> get(r, c) }).toJavaList()) })
        // 3. No same number can appear in the same column.
        Stream.range(0, cols().toLong()).foreachDoEffect(Effect1<Int> { c: Int -> col2state[c] = f.f(Stream.range(0, rows().toLong()).map<Int>(F<Int, Int> { r: Int -> get(r, c) }).toJavaList()) })
        // 1. You need to fill each region with a randomly ordered sequence of numbers.
        Stream.range(0, game.areas.size.toLong()).foreachDoEffect(Effect1<Int> { i: Int? -> area2state[i!!] = f.f(fj.data.List.iterableList<Position>(game.areas.get(i)).map<Int>(F<Position, Int> { p: Position -> get(p) }).toJavaList()) })
    }

    init {
        objArray = IntArray(rows() * cols())
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.size)
        row2state = arrayOfNulls<HintState>(rows())
        col2state = arrayOfNulls<HintState>(cols())
        area2state = arrayOfNulls<HintState>(game.areas.size)
        updateIsSolved()
    }
}