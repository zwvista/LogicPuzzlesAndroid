package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class RobotCrosswordsGameState(game: RobotCrosswordsGame) : CellsGameState<RobotCrosswordsGame, RobotCrosswordsGameMove, RobotCrosswordsGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2horzState = mutableMapOf<Position, HintState>()
    var pos2vertState = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: RobotCrosswordsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != 0 || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: RobotCrosswordsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != 0) return GameOperationType.Invalid
        val o = this[p]
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
        for ((i, a) in game.areas.withIndex()) {
            val nums = a.map { this[it] }
            val nums2 = nums.toSet().toList().sorted()
            // 2. Each 'word' is formed by an uninterrupted sequence of numbers,
            // but in any order.
            val s = if (nums2[0] == 0) HintState.Normal else if (nums2.size == nums.size && (0..<nums2.size - 1).all {
                nums2[it + 1] - nums2[it] == 1
            }) HintState.Complete else HintState.Error
            for (p in a)
                (if (i < game.horzAreaCount) pos2horzState else pos2vertState)[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}