package com.zwstudio.logicpuzzlesandroid.puzzles.thermometers

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ThermometersGameState(game: ThermometersGame) : CellsGameState<ThermometersGame, ThermometersGameMove, ThermometersGameState>(game) {
    var objArray = Array<ThermometersObject>(rows * cols) { ThermometersEmptyObject }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: ThermometersObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: ThermometersObject) {this[p.row, p.col] = obj}

    init {
        for ((p, _) in game.pos2arrow)
            this[p] = ThermometersArrowObject()
        updateIsSolved()
    }

    override fun setObject(move: ThermometersGameMove): GameOperationType {
        if (!isValid(move.p) || this[move.p] === move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ThermometersGameMove): GameOperationType {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val o = this[p]
        move.obj = when (o) {
            is ThermometersEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) ThermometersMarkerObject else ThermometersStarObject()
            is ThermometersStarObject -> if (markerOption == MarkerOptions.MarkerLast) ThermometersMarkerObject else ThermometersEmptyObject
            is ThermometersMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) ThermometersStarObject() else ThermometersEmptyObject
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
                if (this[r, c] is ThermometersStarObject)
                    n1++
            // 3. The numbers on the borders tell you how many Stars there are on that row.
            row2state[r] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (c in 0 until cols) {
            var n1 = 0
            val n2 = game.col2hint[c]
            for (r in 0 until rows)
                if (this[r, c] is ThermometersStarObject)
                    n1++
            // 3. The numbers on the borders tell you how many Stars there are on that column.
            col2state[c] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is ThermometersForbiddenObject)
                    this[r, c] = ThermometersEmptyObject
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[r, c]
                fun hasArrow(): Boolean {
                    var n = 0
                    for (i in 0..<8) {
                        val os = ThermometersGame.offset2[i]
                        var p2 = p + os
                        while (isValid(p2)) {
                            if (this[p2] is ThermometersArrowObject && (game.pos2arrow[p2]!! + 4) % 8 == i)
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
                    val os = ThermometersGame.offset2[game.pos2arrow[p]!!]
                    var p2 = p + os
                    while (isValid(p2)) {
                        if (this[p2] is ThermometersStarObject)
                            n++
                        p2 += os
                    }
                    // 4. Some levels have a variation of these rules: Stars must be pointed
                    // by one and only one Arrow.
                    return game.onlyOneArrow && n == 1 || n >= 1
                }
                if (o is ThermometersStarObject) {
                    // 2. Each star is pointed at by at least one Arrow.
                    val s = if (hasArrow()) AllowedObjectState.Normal else AllowedObjectState.Error
                    o.state = s
                    if (s == AllowedObjectState.Error) isSolved = false
                } else if (o is ThermometersArrowObject) {
                    // 2. Each Arrow points to at least one star.
                    val s = if (hasStar()) AllowedObjectState.Normal else AllowedObjectState.Error
                    o.state = s
                    if (s == AllowedObjectState.Error) isSolved = false
                } else if ((o is ThermometersEmptyObject || o is ThermometersMarkerObject) && allowedObjectsOnly &&
                    (col2state[c] != HintState.Normal || row2state[r] != HintState.Normal || !hasArrow()))
                    this[r, c] = ThermometersForbiddenObject
            }
    }
}