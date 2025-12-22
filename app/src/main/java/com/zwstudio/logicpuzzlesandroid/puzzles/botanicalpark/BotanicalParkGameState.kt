package com.zwstudio.logicpuzzlesandroid.puzzles.botanicalpark

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BotanicalParkGameState(game: BotanicalParkGame) : CellsGameState<BotanicalParkGame, BotanicalParkGameMove, BotanicalParkGameState>(game) {
    var objArray = Array<BotanicalParkObject>(rows * cols) { BotanicalParkEmptyObject }

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
        move.obj = when (val o = this[p]) {
            is BotanicalParkEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) BotanicalParkMarkerObject else BotanicalParkPlantObject()
            is BotanicalParkPlantObject -> if (markerOption == MarkerOptions.MarkerLast) BotanicalParkMarkerObject else BotanicalParkEmptyObject
            is BotanicalParkMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) BotanicalParkPlantObject() else BotanicalParkEmptyObject
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
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is BotanicalParkForbiddenObject)
                    this[r, c] = BotanicalParkEmptyObject
        // 3. Plants cannot touch, not even diagonally.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                fun hasNeighbor() = BotanicalParkGame.offset.any {
                    val p2 = p + it
                    isValid(p2) && this[p2] is BotanicalParkPlantObject
                }
                val o = this[r, c]
                if (o is BotanicalParkPlantObject)
                    o.state = if (!hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is BotanicalParkEmptyObject || o is BotanicalParkMarkerObject) && allowedObjectsOnly && hasNeighbor())
                    this[r, c] = BotanicalParkForbiddenObject
            }
        val n2 = game.plantsInEachArea
        // 2. There is exactly one plant in every row.
        for (r in 0 until rows) {
            var n1 = 0
            for (c in 0 until cols)
                if (this[r, c] is BotanicalParkPlantObject)
                    n1++
            if (n1 != n2) isSolved = false
            for (c in 0 until cols) {
                val o = this[r, c]
                if (o is BotanicalParkPlantObject)
                    o.state = if (o.state == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is BotanicalParkEmptyObject || o is BotanicalParkMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    this[r, c] = BotanicalParkForbiddenObject
            }
        }
        // 2. There is exactly one plant in every column.
        for (c in 0 until cols) {
            var n1 = 0
            for (r in 0 until rows)
                if (this[r, c] is BotanicalParkPlantObject)
                    n1++
            if (n1 != n2) isSolved = false
            for (r in 0 until rows) {
                val o = this[r, c]
                if (o is BotanicalParkPlantObject)
                    o.state = if (o.state == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is BotanicalParkEmptyObject || o is BotanicalParkMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    this[r, c] = BotanicalParkForbiddenObject
            }
        }
        // 2. Each arrow points to at least one plant.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[r, c]
                fun hasPlant(): Boolean {
                    var n = 0
                    val os = BotanicalParkGame.offset[game.pos2arrow[p]!!]
                    var p2 = p + os
                    while (isValid(p2)) {
                        if (this[p2] is BotanicalParkPlantObject)
                            n++
                        p2 += os
                    }
                    return n >= 1
                }
                if (o is BotanicalParkArrowObject) {
                    // 2. Each Arrow points to at least one star.
                    val s = if (hasPlant()) AllowedObjectState.Normal else AllowedObjectState.Error
                    o.state = s
                    if (s == AllowedObjectState.Error) isSolved = false
                }
            }
    }
}