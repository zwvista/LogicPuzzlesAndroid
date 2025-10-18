package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenstars

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class HiddenStarsGameState(game: HiddenStarsGame) : CellsGameState<HiddenStarsGame, HiddenStarsGameMove, HiddenStarsGameState>(game) {
    var objArray = Array<HiddenStarsObject>(rows * cols) { HiddenStarsEmptyObject }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: HiddenStarsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: HiddenStarsObject) {this[p.row, p.col] = obj}

    init {
        for ((p, _) in game.pos2arrow)
            this[p] = HiddenStarsArrowObject()
        updateIsSolved()
    }

    override fun setObject(move: HiddenStarsGameMove): GameOperationType {
        if (!isValid(move.p) || this[move.p] === move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: HiddenStarsGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val o = this[p]
        move.obj = when (o) {
            is HiddenStarsEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) HiddenStarsMarkerObject else HiddenStarsStarObject()
            is HiddenStarsStarObject -> if (markerOption == MarkerOptions.MarkerLast) HiddenStarsMarkerObject else HiddenStarsEmptyObject
            is HiddenStarsMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) HiddenStarsStarObject() else HiddenStarsEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 2/Hidden Stars

        Summary
        Each Arrow points to a Star and every Star has an arrow pointing at it

        Description
        1. In the board you have to find hidden stars.
        2. Each star is pointed at by at least one Arrow and each Arrow points
           to at least one star.
        3. The number on the borders tell you how many Stars there on that row
           or column.

        Variant
        4. Some levels have a variation of these rules: Stars must be pointed
           by one and only one Arrow.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows) {
            var n1 = 0
            val n2 = game.row2hint[r]
            for (c in 0 until cols)
                if (this[r, c] is HiddenStarsStarObject)
                    n1++
            // 3. The numbers on the borders tell you how many Stars there are on that row.
            row2state[r] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (c in 0 until cols) {
            var n1 = 0
            val n2 = game.col2hint[c]
            for (r in 0 until rows)
                if (this[r, c] is HiddenStarsStarObject)
                    n1++
            // 3. The numbers on the borders tell you how many Stars there are on that column.
            col2state[c] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is HiddenStarsForbiddenObject)
                    this[r, c] = HiddenStarsEmptyObject
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[r, c]
                fun hasArrow(): Boolean {
                    var n = 0
                    for (i in 0..<8) {
                        val os = HiddenStarsGame.offset2[i]
                        var p2 = p + os
                        while (isValid(p2)) {
                            if (this[p2] is HiddenStarsArrowObject && (game.pos2arrow[p2]!! + 4) % 8 == i)
                                n++
                            p2 += os
                        }
                    }
                    // 4. Some levels have a variation of these rules: Stars must be pointed
                    // by one and only one Arrow.
                    return game.onlyOneArrow && n == 1 || n >= 1
                }
                fun hasStar(): Boolean {
                    var n = 0
                    val os = HiddenStarsGame.offset2[game.pos2arrow[p]!!]
                    var p2 = p + os
                    while (isValid(p2)) {
                        if (this[p2] is HiddenStarsStarObject)
                            n++
                        p2 += os
                    }
                    // 4. Some levels have a variation of these rules: Stars must be pointed
                    // by one and only one Arrow.
                    return game.onlyOneArrow && n == 1 || n >= 1
                }
                if (o is HiddenStarsStarObject) {
                    // 2. Each star is pointed at by at least one Arrow.
                    val s = if (hasArrow()) AllowedObjectState.Normal else AllowedObjectState.Error
                    o.state = s
                    if (s == AllowedObjectState.Error) isSolved = false
                } else if (o is HiddenStarsArrowObject) {
                    // 2. Each Arrow points to at least one star.
                    val s = if (hasStar()) AllowedObjectState.Normal else AllowedObjectState.Error
                    o.state = s
                    if (s == AllowedObjectState.Error) isSolved = false
                } else if ((o is HiddenStarsEmptyObject || o is HiddenStarsMarkerObject) && allowedObjectsOnly &&
                    (col2state[c] != HintState.Normal || row2state[r] != HintState.Normal || !hasArrow()))
                    this[r, c] = HiddenStarsForbiddenObject
            }
    }
}