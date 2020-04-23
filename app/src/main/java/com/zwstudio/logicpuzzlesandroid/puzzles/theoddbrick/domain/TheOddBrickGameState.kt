package com.zwstudio.logicpuzzlesandroid.puzzles.theoddbrick.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.data.Set
import fj.data.Stream

class TheOddBrickGameState(game: TheOddBrickGame) : CellsGameState<TheOddBrickGame, TheOddBrickGameMove, TheOddBrickGameState>(game) {
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

    fun setObject(move: TheOddBrickGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) != 0 || get(p) == move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: TheOddBrickGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) != 0) return false
        val o = get(p)
        move.obj = (o + 1) % (cols() + 1)
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
        val f: F<List<Int>, HintState> = F<List<Int>, HintState> { nums: List<Int> ->
            val nums2 = Set.iterableSet(Ord.intOrd, nums).toJavaList()
            // 3. Each row and column contains numbers 1 to N, where N is the side of
            // the board.
            val s: HintState = if (nums2[0] == 0) HintState.Normal else if (nums2.size == nums.size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            s
        }
        Stream.range(0, rows().toLong()).foreachDoEffect(Effect1<Int> { r: Int -> row2state[r] = f.f(Stream.range(0, cols().toLong()).map<Int>(F<Int, Int> { c: Int -> get(r, c) }).toJavaList()) })
        Stream.range(0, cols().toLong()).foreachDoEffect(Effect1<Int> { c: Int -> col2state[c] = f.f(Stream.range(0, rows().toLong()).map<Int>(F<Int, Int> { r: Int -> get(r, c) }).toJavaList()) })
        Stream.range(0, game.areas.size.toLong()).foreachDoEffect(Effect1<Int> { i: Int? ->
            val nums = fj.data.List.iterableList<Position>(game.areas.get(i)).map<Int>(F<Position, Int> { p: Position -> get(p) }).toJavaList()
            // 2. Each 2*1 brick contains and odd and an even number, while 1*1 bricks
            // can contain any number.
            area2state[i!!] = if (nums.contains(0)) HintState.Normal else if (nums.size == 1 || nums[0] % 2 == 0 && nums[1] % 2 == 1 || nums[0] % 2 == 1 && nums[1] % 2 == 0) HintState.Complete else HintState.Error
            if (area2state[i] != HintState.Complete) isSolved = false
        })
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