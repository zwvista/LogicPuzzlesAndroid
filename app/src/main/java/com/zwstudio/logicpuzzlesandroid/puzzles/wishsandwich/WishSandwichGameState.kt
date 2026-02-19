package com.zwstudio.logicpuzzlesandroid.puzzles.wishsandwich

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class WishSandwichGameState(game: WishSandwichGame) : CellsGameState<WishSandwichGame, WishSandwichGameMove, WishSandwichGameState>(game) {
    var objArray = Array(rows * cols) { WishSandwichObject.Empty }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: WishSandwichObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: WishSandwichObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: WishSandwichGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: WishSandwichGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            WishSandwichObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) WishSandwichObject.Marker else WishSandwichObject.Bread
            WishSandwichObject.Bread -> WishSandwichObject.Ham
            WishSandwichObject.Ham -> if (markerOption == MarkerOptions.MarkerLast) WishSandwichObject.Marker else WishSandwichObject.Empty
            WishSandwichObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) WishSandwichObject.Bread else WishSandwichObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 3/Wish Sandwich

        Summary
        ...ever heard of it ?

        Description
        1. Each row and column contains two Slices of Bread and N-3 Pieces of Pieces of Ham
           (N being the board size). i.e. a board side 6, will have 3 Pieces of Ham.
        2. A number at the edge indicates how many Pieces of Ham you managed to put
           between the two Slices of Bread in that row or column.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (this[p]) {
                    WishSandwichObject.Forbidden ->
                        this[p] = WishSandwichObject.Empty
                    WishSandwichObject.Bread, WishSandwichObject.Ham ->
                        pos2state[p] = AllowedObjectState.Normal
                    else -> {}
                }
            }
        for (r in 0 until rows) {
            val breads = mutableListOf<Position>()
            val hams = mutableListOf<Position>()
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (this[p]) {
                    WishSandwichObject.Bread -> breads.add(p)
                    WishSandwichObject.Ham -> hams.add(p)
                    else -> {}
                }
            }
            if (breads.size > 2)
                for (p in breads)
                    pos2state[p] = AllowedObjectState.Error
            if (hams.size > rows - 3)
                for (p in hams)
                    pos2state[p] = AllowedObjectState.Error
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
                    (0 until cols).filter { this[r, it] == WishSandwichObject.Empty }.forEach {
                        this[r, it] = WishSandwichObject.Forbidden
                    }
            }
        }
        for (c in 0 until cols) {
            val breads = mutableListOf<Position>()
            val hams = mutableListOf<Position>()
            for (r in 0 until rows) {
                val p = Position(r, c)
                when (this[p]) {
                    WishSandwichObject.Bread -> breads.add(p)
                    WishSandwichObject.Ham -> hams.add(p)
                    else -> {}
                }
            }
            if (breads.size > 2)
                for (p in breads)
                    pos2state[p] = AllowedObjectState.Error
            if (hams.size > rows - 3)
                for (p in hams)
                    pos2state[p] = AllowedObjectState.Error
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
                    (0 until rows).filter { this[it, c] == WishSandwichObject.Empty }.forEach {
                        this[it, c] = WishSandwichObject.Forbidden
                    }
            }
        }
    }
}