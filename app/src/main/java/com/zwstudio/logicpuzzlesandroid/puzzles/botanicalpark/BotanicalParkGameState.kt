package com.zwstudio.logicpuzzlesandroid.puzzles.botanicalpark

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BotanicalParkGameState(game: BotanicalParkGame) : CellsGameState<BotanicalParkGame, BotanicalParkGameMove, BotanicalParkGameState>(game) {
    var objArray = Array<BotanicalParkObject>(rows * cols) { BotanicalParkEmptyObject }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: BotanicalParkObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: BotanicalParkObject) {this[p.row, p.col] = obj}

    init {
        for ((p, _) in game.pos2arrow)
            this[p] = BotanicalParkArrowObject()
        updateIsSolved()
    }

    override fun setObject(move: BotanicalParkGameMove): GameOperationType {
        if (!isValid(move.p) || this[move.p] === move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: BotanicalParkGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val o = this[p]
        move.obj = when (o) {
            is BotanicalParkEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) BotanicalParkMarkerObject else BotanicalParkTreeObject()
            is BotanicalParkTreeObject -> if (markerOption == MarkerOptions.MarkerLast) BotanicalParkMarkerObject else BotanicalParkEmptyObject
            is BotanicalParkMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) BotanicalParkTreeObject() else BotanicalParkEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 12/Botanical Park

        Summary
        Excuse me sir ? Do you know where the Harpagophytum Procumbens is ?

        Description
        1. The board represents a Botanical Park, with arrows pointing to the
           different plants.
        2. Each arrow points to at least one plant and there is exactly one
           plant in every row and in every column.
        3. Plants cannot touch, not even diagonally.

        Variant
        4. Puzzle with side 9 or bigger have TWO plants in every row and column.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows) {
            var n1 = 0
            val n2 = game.row2hint[r]
            for (c in 0 until cols)
                if (this[r, c] is BotanicalParkTreeObject)
                    n1++
            // 3. The numbers on the borders tell you how many Stars there are on that row.
            row2state[r] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (c in 0 until cols) {
            var n1 = 0
            val n2 = game.col2hint[c]
            for (r in 0 until rows)
                if (this[r, c] is BotanicalParkTreeObject)
                    n1++
            // 3. The numbers on the borders tell you how many Stars there are on that column.
            col2state[c] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is BotanicalParkForbiddenObject)
                    this[r, c] = BotanicalParkEmptyObject
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[r, c]
                fun hasArrow(): Boolean {
                    var n = 0
                    for (i in 0..<8) {
                        val os = BotanicalParkGame.offset2[i]
                        var p2 = p + os
                        while (isValid(p2)) {
                            if (this[p2] is BotanicalParkArrowObject && (game.pos2arrow[p2]!! + 4) % 8 == i)
                                n++
                            p2 += os
                        }
                    }
                    // 4. Some levels have a variation of these rules: Stars must be pointed
                    // by one and only one Arrow.
                    return game.onlyOneArrow && n == 1 || n >= 1
                }
                fun hasTree(): Boolean {
                    var n = 0
                    val os = BotanicalParkGame.offset2[game.pos2arrow[p]!!]
                    var p2 = p + os
                    while (isValid(p2)) {
                        if (this[p2] is BotanicalParkTreeObject)
                            n++
                        p2 += os
                    }
                    // 4. Some levels have a variation of these rules: Stars must be pointed
                    // by one and only one Arrow.
                    return game.onlyOneArrow && n == 1 || n >= 1
                }
                if (o is BotanicalParkTreeObject) {
                    // 2. Each star is pointed at by at least one Arrow.
                    val s = if (hasArrow()) AllowedObjectState.Normal else AllowedObjectState.Error
                    o.state = s
                    if (s == AllowedObjectState.Error) isSolved = false
                } else if (o is BotanicalParkArrowObject) {
                    // 2. Each Arrow points to at least one star.
                    val s = if (hasTree()) AllowedObjectState.Normal else AllowedObjectState.Error
                    o.state = s
                    if (s == AllowedObjectState.Error) isSolved = false
                } else if ((o is BotanicalParkEmptyObject || o is BotanicalParkMarkerObject) && allowedObjectsOnly &&
                    (col2state[c] != HintState.Normal || row2state[r] != HintState.Normal || !hasArrow()))
                    this[r, c] = BotanicalParkForbiddenObject
            }
    }
}