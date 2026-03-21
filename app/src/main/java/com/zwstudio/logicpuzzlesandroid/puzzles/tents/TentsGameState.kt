package com.zwstudio.logicpuzzlesandroid.puzzles.tents

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TentsGameState(game: TentsGame) : CellsGameState<TentsGame, TentsGameMove, TentsGameState>(game) {
    var objArray = Array(rows * cols) { TentsObject.Empty }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: TentsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: TentsObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.trees)
            this[p] = TentsObject.Tree
        updateIsSolved()
    }

    override fun setObject(move: TentsGameMove): GameOperationType {
        if (!isValid(move.p) || this[move.p] === move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TentsGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            TentsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) TentsObject.Marker else TentsObject.Tent
            TentsObject.Tent -> if (markerOption == MarkerOptions.MarkerLast) TentsObject.Marker else TentsObject.Empty
            TentsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) TentsObject.Tent else TentsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 1/Tents

        Summary
        Each camper wants his shade. But his privacy too!

        Description
        1. The board represents a camping field with many Trees. Campers want to set
           their Tent in the shade, horizontally or vertically adjacent to a Tree(not
           diagonally).
        2. At the same time they need their privacy, so a Tent can't have any other
           Tents near them, not even diagonally.
        3. The numbers on the borders tell you how many Tents there are in that row
           or column.
        4. Finally, keep in mind these two rules:
        5. Each Tree has at least one Tent touching it, horizontally or vertically.
        6. There's the same exact numbers of Trees and Tents in the Camping Area.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows) {
            var n1 = 0
            val n2 = game.row2hint[r]
            for (c in 0 until cols)
                if (this[r, c] == TentsObject.Tent)
                    n1++
            // 3. The numbers on the borders tell you how many Tents there are in that row.
            val s = if (n2 == TentsGame.PUZ_UNKNOWN || n1 == n2) HintState.Complete else if (n1 < n2) HintState.Normal else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (c in 0 until cols) {
            var n1 = 0
            val n2 = game.col2hint[c]
            for (r in 0 until rows)
                if (this[r, c] == TentsObject.Tent)
                    n1++
            // 3. The numbers on the borders tell you how many Tents there are in that column.
            val s = if (n2 == TentsGame.PUZ_UNKNOWN || n1 == n2) HintState.Complete else if (n1 < n2) HintState.Normal else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] == TentsObject.Forbidden)
                    this[r, c] = TentsObject.Empty
        var (nTree, nTent) = 0 to 0
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                fun hasTree(): Boolean =
                    TentsGame.offset.any {
                        val p2 = p + it
                        isValid(p2) && this[p2] == TentsObject.Tree
                    }
                fun hasTent(isTree: Boolean): Boolean =
                    (if (isTree) TentsGame.offset else TentsGame.offset2).any {
                        val p2 = p + it
                        isValid(p2) && this[p2] == TentsObject.Tent
                    }
                when (this[p]) {
                    TentsObject.Tent -> {
                        nTent++
                        // 1. The board represents a camping field with many Trees. Campers want to set
                        // their Tent in the shade, horizontally or vertically adjacent to a Tree(not
                        // diagonally).
                        // 2. At the same time they need their privacy, so a Tent can't have any other
                        // Tents near them, not even diagonally.
                        val s =
                            if (hasTree() && !hasTent(false)) AllowedObjectState.Normal else AllowedObjectState.Error
                        pos2state[p] = s
                        if (s == AllowedObjectState.Error) isSolved = false
                    }
                    TentsObject.Tree -> {
                        nTree++
                        // 5. Each Tree has at least one Tent touching it, horizontally or vertically.
                        val s =
                            if (hasTent(true)) AllowedObjectState.Normal else AllowedObjectState.Error
                        pos2state[p] = s
                        if (s == AllowedObjectState.Error) isSolved = false
                    }
                    TentsObject.Empty, TentsObject.Marker ->
                        if (allowedObjectsOnly &&
                            (col2state[c] != HintState.Normal && game.col2hint[c] != TentsGame.PUZ_UNKNOWN ||
                            row2state[r] != HintState.Normal && game.row2hint[r] != TentsGame.PUZ_UNKNOWN ||
                            !hasTree() || hasTent(false))
                        )
                            this[r, c] = TentsObject.Forbidden
                    else -> {}
                }
            }
        // 6. There's the same exact numbers of Trees and Tents in the Camping Area.
        if (nTree != nTent) isSolved = false
    }
}