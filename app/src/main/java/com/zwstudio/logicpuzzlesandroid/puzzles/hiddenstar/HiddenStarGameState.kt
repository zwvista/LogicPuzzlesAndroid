package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenstar

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class HiddenStarGameState(game: HiddenStarGame) : CellsGameState<HiddenStarGame, HiddenStarGameMove, HiddenStarGameState>(game) {
    var objArray = Array<HiddenStarObject>(rows * cols) { HiddenStarEmptyObject }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: HiddenStarObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: HiddenStarObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2tree)
            this[p] = HiddenStarTreeObject()
        updateIsSolved()
    }

    override fun setObject(move: HiddenStarGameMove): Boolean {
        if (!isValid(move.p) || this[move.p] === move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    override fun switchObject(move: HiddenStarGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return false
        val o = this[p]
        move.obj = when (o) {
            is HiddenStarEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) HiddenStarMarkerObject else HiddenStarTentObject()
            is HiddenStarTentObject -> if (markerOption == MarkerOptions.MarkerLast) HiddenStarMarkerObject else HiddenStarEmptyObject
            is HiddenStarMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) HiddenStarTentObject() else HiddenStarEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/HiddenStar

        Summary
        Each camper wants to put his Tent under the shade of a Tree. But he also
        wants his privacy!

        Description
        1. The board represents a camping field with many Trees. Campers want to set
           their Tent in the shade, horizontally or vertically adjacent to a Tree(not
           diagonally).
        2. At the same time they need their privacy, so a Tent can't have any other
           HiddenStar near them, not even diagonally.
        3. The numbers on the borders tell you how many HiddenStar there are in that row
           or column.
        4. Finally, each Tree has at least one Tent touching it, horizontally or
           vertically.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows) {
            var n1 = 0
            val n2 = game.row2hint[r]
            for (c in 0 until cols)
                if (this[r, c] is HiddenStarTentObject)
                    n1++
            // 3. The numbers on the borders tell you how many HiddenStar there are in that row.
            row2state[r] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (c in 0 until cols) {
            var n1 = 0
            val n2 = game.col2hint[c]
            for (r in 0 until rows)
                if (this[r, c] is HiddenStarTentObject)
                    n1++
            // 3. The numbers on the borders tell you how many HiddenStar there are in that column.
            col2state[c] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is HiddenStarForbiddenObject)
                    this[r, c] = HiddenStarEmptyObject
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[r, c]
                fun hasTree(): Boolean {
                    for (os in HiddenStarGame.offset) {
                        val p2 = p + os
                        if (isValid(p2) && this[p2] is HiddenStarTreeObject)
                            return true
                    }
                    return false
                }
                fun hasTent(isTree: Boolean): Boolean {
                    for (os in if (isTree) HiddenStarGame.offset else HiddenStarGame.offset2) {
                        val p2 = p + os
                        if (isValid(p2) && this[p2] is HiddenStarTentObject)
                            return true
                    }
                    return false
                }
                if (o is HiddenStarTentObject) {
                    // 1. The board represents a camping field with many Trees. Campers want to set
                    // their Tent in the shade, horizontally or vertically adjacent to a Tree(not
                    // diagonally).
                    // 2. At the same time they need their privacy, so a Tent can't have any other
                    // HiddenStar near them, not even diagonally.
                    val s = if (hasTree() && !hasTent(false)) AllowedObjectState.Normal else AllowedObjectState.Error
                    o.state = s
                    if (s == AllowedObjectState.Error) isSolved = false
                } else if (o is HiddenStarTreeObject) {
                    // 4. Finally, each Tree has at least one Tent touching it, horizontally or
                    // vertically.
                    val s = if (hasTent(true)) AllowedObjectState.Normal else AllowedObjectState.Error
                    o.state = s
                    if (s == AllowedObjectState.Error) isSolved = false
                } else if ((o is HiddenStarEmptyObject || o is HiddenStarMarkerObject) && allowedObjectsOnly &&
                    (col2state[c] != HintState.Normal || row2state[r] != HintState.Normal || !hasTree() || hasTent(false)))
                    this[r, c] = HiddenStarForbiddenObject
            }
    }
}