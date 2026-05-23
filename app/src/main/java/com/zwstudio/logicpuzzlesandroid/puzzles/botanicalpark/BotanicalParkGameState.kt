package com.zwstudio.logicpuzzlesandroid.puzzles.botanicalpark

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BotanicalParkGameState(game: BotanicalParkGame) : CellsGameState<BotanicalParkGame, BotanicalParkGameMove, BotanicalParkGameState>(game) {
    val objArray = Array<BotanicalParkObject>(rows * cols) { BotanicalParkObject.Empty }
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: BotanicalParkObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: BotanicalParkObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2arrow.keys)
            this[p] = BotanicalParkObject.Arrow
        updateIsSolved()
    }

    override fun setObject(move: BotanicalParkGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: BotanicalParkGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            BotanicalParkObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) BotanicalParkObject.Marker else BotanicalParkObject.Plant
            BotanicalParkObject.Plant -> if (markerOption == MarkerOptions.MarkerLast) BotanicalParkObject.Marker else BotanicalParkObject.Empty
            BotanicalParkObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) BotanicalParkObject.Plant else BotanicalParkObject.Empty
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
        for (r in 0..<rows)
            for (c in 0..<cols)
                if (this[r, c] == BotanicalParkObject.Forbidden)
                    this[r, c] = BotanicalParkObject.Empty
        // 3. Plants cannot touch, not even diagonally.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                fun touchPlant() = BotanicalParkGame.offset.any {
                    val p2 = p + it
                    isValid(p2) && this[p2] == BotanicalParkObject.Plant
                }
                when (this[p]) {
                    BotanicalParkObject.Plant -> {
                        val s = if (!touchPlant()) AllowedObjectState.Normal else AllowedObjectState.Error
                        pos2state[p] = s
                        if (s == AllowedObjectState.Error) isSolved = false
                    }
                    BotanicalParkObject.Empty, BotanicalParkObject.Marker ->
                        if (allowedObjectsOnly && touchPlant())
                            this[p] = BotanicalParkObject.Forbidden
                    else -> {}
                }
            }
        val n2 = game.plantsInEachArea
        // 2. There is exactly one plant in every row.
        for (r in 0..<rows) {
            val n1 = (0..<cols).count { this[r, it] == BotanicalParkObject.Plant }
            if (n1 != n2) isSolved = false
            for (c in 0..<cols) {
                val p = Position(r, c)
                when (this[p]) {
                    BotanicalParkObject.Plant ->
                        pos2state[p] = if (pos2state[p] == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                    BotanicalParkObject.Empty, BotanicalParkObject.Marker ->
                        if (n1 >= n2 && allowedObjectsOnly)
                            this[p] = BotanicalParkObject.Forbidden
                    else -> {}
                }
            }
        }
        // 2. There is exactly one plant in every column.
        for (c in 0..<cols) {
            val n1 = (0..<rows).count { this[it, c] == BotanicalParkObject.Plant }
            if (n1 != n2) isSolved = false
            for (r in 0..<rows) {
                val p = Position(r, c)
                when (this[p]) {
                    BotanicalParkObject.Plant ->
                        pos2state[p] = if (pos2state[p] == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                    BotanicalParkObject.Empty, BotanicalParkObject.Marker ->
                        if (n1 >= n2 && allowedObjectsOnly)
                            this[p] = BotanicalParkObject.Forbidden
                    else -> {}
                }
            }
        }
        // 2. Each arrow points to at least one plant.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p] != BotanicalParkObject.Arrow) continue
                // 2. Each Arrow points to at least one star.
                val s = if (run {
                    var n = 0
                    val os = BotanicalParkGame.offset[game.pos2arrow[p]!!]
                    var p2 = p + os
                    while (isValid(p2)) {
                        if (this[p2] == BotanicalParkObject.Plant)
                            n++
                        p2 += os
                    }
                    n >= 1
                }) AllowedObjectState.Normal else AllowedObjectState.Error
                pos2state[p] = s
                if (s == AllowedObjectState.Error) isSolved = false
            }
    }
}