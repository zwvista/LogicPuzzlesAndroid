package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.data.Set
import java.util.*

class RobotCrosswordsGameState(game: RobotCrosswordsGame) : CellsGameState<RobotCrosswordsGame?, RobotCrosswordsGameMove?, RobotCrosswordsGameState?>(game) {
    var objArray: IntArray
    var pos2horzState: Map<Position, HintState> = HashMap<Position, HintState>()
    var pos2vertState: Map<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: Int) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Int) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: RobotCrosswordsGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) != 0 || get(p) == move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: RobotCrosswordsGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) != 0) return false
        val o = get(p)
        move.obj = (o + 1) % 10
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/RobotCrosswords

        Summary
        BZZZZliip 4 across?

        Description
        1. In a possible crossword for Robots, letters are substituted with digits.
        2. Each 'word' is formed by an uninterrupted sequence of numbers (i.e.
           2-3-4-5), but in any order (i.e. 3-4-2-5).
    */
    private fun updateIsSolved() {
        isSolved = true
        for (i in game.areas.indices) {
            val a: List<Position> = game.areas.get(i)
            val nums = fj.data.List.iterableList(a).map<Int>(F<Position, Int> { p: Position -> get(p) }).toJavaList()
            val nums2 = Set.iterableSet(Ord.intOrd, nums).toJavaList()
            // 2. Each 'word' is formed by an uninterrupted sequence of numbers,
            // but in any order.
            val s: HintState = if (nums2[0] == 0) HintState.Normal else if (nums2.size == nums.size) HintState.Complete else HintState.Error
            for (p in a) (if (i < game.horzAreaCount) pos2horzState else pos2vertState).put(p, s)
            if (s != HintState.Complete) isSolved = false
        }
    }

    init {
        objArray = IntArray(rows() * cols())
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.size)
        updateIsSolved()
    }
}