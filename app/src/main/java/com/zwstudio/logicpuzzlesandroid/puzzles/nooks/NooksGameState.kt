package com.zwstudio.logicpuzzlesandroid.puzzles.nooks

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NooksGameState(game: NooksGame) : CellsGameState<NooksGame, NooksGameMove, NooksGameState>(game) {
    var objArray = Array<NooksObject>(rows * cols) { NooksEmptyObject }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: NooksObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: NooksObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: NooksGameMove): GameOperationType {
        if (this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: NooksGameMove): GameOperationType {
        val p = move.p
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is NooksEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) NooksMarkerObject else NooksBreadObject()
            is NooksBreadObject -> NooksHamObject()
            is NooksHamObject -> if (markerOption == MarkerOptions.MarkerLast) NooksMarkerObject else NooksEmptyObject
            is NooksMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) NooksBreadObject() else NooksEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 17/Nooks

        Summary
        ...in a forest

        Description
        1. Fill some tiles with hedges, so that each number (where someone is playing hide and seek)
           finds itself in the nook.
        2. a Nook is a dead end, one tile wide, with a number in it.
        3. a Nook contains a number that shows you how many tiles can be seen in a straight line from
           there, including the tile itself.
        4. The resulting maze should be a single one-tile path connected horizontally or vertically
           where there are no 2x2 areas of the same type (hedge or path).
        5. No area in the maze can have the characteristics of a Nook without a number in it.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                this[r, c] = when (val o = this[r, c]) {
                    is NooksForbiddenObject -> NooksEmptyObject
                    is NooksBreadObject -> NooksBreadObject()
                    is NooksHamObject -> NooksHamObject()
                    else -> o
                }
        for (r in 0 until rows) {
            val breads = mutableListOf<Position>()
            val hams = mutableListOf<Position>()
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (this[p]) {
                    is NooksBreadObject -> breads.add(p)
                    is NooksHamObject -> hams.add(p)
                    else -> {}
                }
            }
            if (breads.size > 2)
                for (p in breads)
                    this[p] = NooksBreadObject(state = AllowedObjectState.Error)
            if (hams.size > rows - 3)
                for (p in hams)
                    this[p] = NooksHamObject(state = AllowedObjectState.Error)
            if (breads.size != 2) {
                isSolved = false
                row2state[r] = HintState.Normal
            } else {
                val n2 = game.row2hint[r]
                if (n2 < 0) continue
                // 1. Each row and column contains two Slices of Bread and N-3 Pieces of Pieces of Ham
                //    (N being the board size). i.e. a board side 6, will have 3 Pieces of Ham.
                val n1 = hams.count { it.col > breads[0].col && it.col < breads[1].col }
                val s = if (n1 == n2) HintState.Complete else HintState.Error
                row2state[r] = s
                if (s != HintState.Complete) isSolved = false
                if (allowedObjectsOnly && hams.size == rows - 3)
                    (0 until cols).filter { this[r, it] is NooksEmptyObject }.forEach {
                        this[r, it] = NooksForbiddenObject
                    }
            }
        }
        for (c in 0 until cols) {
            val breads = mutableListOf<Position>()
            val hams = mutableListOf<Position>()
            for (r in 0 until rows) {
                val p = Position(r, c)
                when (this[p]) {
                    is NooksBreadObject -> breads.add(p)
                    is NooksHamObject -> hams.add(p)
                    else -> {}
                }
            }
            if (breads.size > 2)
                for (p in breads)
                    this[p] = NooksBreadObject(state = AllowedObjectState.Error)
            if (hams.size > rows - 3)
                for (p in hams)
                    this[p] = NooksHamObject(state = AllowedObjectState.Error)
            if (breads.size != 2) {
                isSolved = false
                col2state[c] = HintState.Normal
            } else {
                val n2 = game.col2hint[c]
                if (n2 < 0) continue
                // 1. Each row and column contains two Slices of Bread and N-3 Pieces of Pieces of Ham
                //    (N being the board size). i.e. a board side 6, will have 3 Pieces of Ham.
                val n1 = hams.count { it.row > breads[0].row && it.row < breads[1].row }
                val s = if (n1 == n2) HintState.Complete else HintState.Error
                col2state[c] = s
                if (s != HintState.Complete) isSolved = false
                if (allowedObjectsOnly && hams.size == rows - 3)
                    (0 until rows).filter { this[it, c] is NooksEmptyObject }.forEach {
                        this[it, c] = NooksForbiddenObject
                    }
            }
        }
    }
}