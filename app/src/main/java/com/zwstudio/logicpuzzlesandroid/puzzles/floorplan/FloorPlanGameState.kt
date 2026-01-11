package com.zwstudio.logicpuzzlesandroid.puzzles.floorplan

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FloorPlanGameState(game: FloorPlanGame) : CellsGameState<FloorPlanGame, FloorPlanGameMove, FloorPlanGameState>(game) {
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

    override fun setObject(move: FloorPlanGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != 0 || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FloorPlanGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != 0) return GameOperationType.Invalid
        val o = this[p]
        move.obj = (o + 1) % 10
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 1/Floor Plan

        Summary
        Blueprints to fill in

        Description
        1. The board represents a blueprint of an office floor.
        2. Cells with a number represent an office. On the floor every office is
           interconnected and can be reached by every other office.
        3. The number on a cell indicates how many offices it connects to. No two
           offices with the same number can be adjacent.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (i in game.areas.indices) {
            val a = game.areas[i]
            val nums = a.map { this[it] }
            val nums2 = nums.toSet().toList()
            // 2. Each 'word' is formed by an uninterrupted sequence of numbers,
            // but in any order.
            val s = if (nums2[0] == 0) HintState.Normal else if (nums2.size == nums.size) HintState.Complete else HintState.Error
            for (p in a)
                (if (i < game.horzAreaCount) pos2horzState else pos2vertState)[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}