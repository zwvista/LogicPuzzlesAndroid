package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BootyIslandGameState(game: BootyIslandGame) : CellsGameState<BootyIslandGame, BootyIslandGameMove, BootyIslandGameState>(game) {
    val objArray = Array(rows * cols) { BootyIslandObject.Empty }
    val pos2stateHint = mutableMapOf<Position, HintState>()
    val pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    init {
        for (p in game.pos2hint.keys)
            this[p] = BootyIslandObject.Hint
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: BootyIslandObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: BootyIslandObject) {this[p.row, p.col] = obj}

    override fun setObject(move: BootyIslandGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == BootyIslandObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: BootyIslandGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == BootyIslandObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            BootyIslandObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) BootyIslandObject.Marker else BootyIslandObject.Treasure
            BootyIslandObject.Treasure -> if (markerOption == MarkerOptions.MarkerLast) BootyIslandObject.Marker else BootyIslandObject.Empty
            BootyIslandObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) BootyIslandObject.Treasure else BootyIslandObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/Booty Island

        Summary
        Overcrowded Piracy

        Description
        1. Overcrowded by Greedy Pirates (tm), this land has Treasures buried
           almost everywhere and the relative maps scattered around.
        2. In fact there's only one Treasure for each row and for each column.
        3. On the island you can see maps with a number: these tell you how
           many steps are required, horizontally or vertically, to reach a
           Treasure.
        4. For how stupid the Pirates are, they don't bury their Treasures
           touching each other, even diagonally, however at times they are so
           stupid that two or more maps point to the same Treasure!

        Bigger Islands
        5. On bigger islands, there will be two Treasures per row and column.
        6. In this case, the number on the map doesn't necessarily point to the
           closest Treasure on that row or column.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == BootyIslandObject.Forbidden)
                    this[p] = BootyIslandObject.Empty
                else if (o == BootyIslandObject.Treasure)
                    pos2stateAllowed[p] = AllowedObjectState.Normal
            }
        // 4. Pirates don't bury their Treasures touching each other, even diagonally.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                fun hasNeighbor(): Boolean =
                    BootyIslandGame.offset.any {
                        val p2 = p + it
                        isValid(p2) && this[p2] == BootyIslandObject.Treasure
                    }
                val o = this[p]
                if (o == BootyIslandObject.Treasure) {
                    val s = if (pos2stateAllowed[p] == AllowedObjectState.Normal && !hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                    pos2stateAllowed[p] = s
                    if (s == AllowedObjectState.Error) isSolved = false
                } else if ((o == BootyIslandObject.Empty || o == BootyIslandObject.Marker) && allowedObjectsOnly && hasNeighbor())
                    this[p] = BootyIslandObject.Forbidden
            }
        val n2 = game.treasuresInEachArea
        // 2. In fact there's only one Treasure for each row.
        for (r in 0..<rows) {
            var n1 = 0
            for (c in 0..<cols)
                if (this[r, c] == BootyIslandObject.Treasure)
                    n1++
            if (n1 != n2) isSolved = false
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == BootyIslandObject.Treasure)
                    pos2stateAllowed[p] = if (pos2stateAllowed[p] == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o == BootyIslandObject.Empty || o == BootyIslandObject.Marker) && n1 == n2 && allowedObjectsOnly)
                    this[p] = BootyIslandObject.Forbidden
            }
        }
        // 2. In fact there's only one Treasure for each column.
        for (c in 0..<cols) {
            var n1 = 0
            for (r in 0..<rows)
                if (this[r, c] == BootyIslandObject.Treasure)
                    n1++
            if (n1 != n2) isSolved = false
            for (r in 0..<rows) {
                val p = Position(r, c)
                val o = this[p]
                if (o == BootyIslandObject.Treasure)
                    pos2stateAllowed[p] = if (pos2stateAllowed[p] == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o == BootyIslandObject.Empty || o == BootyIslandObject.Marker) && n1 == n2 && allowedObjectsOnly)
                    this[p] = BootyIslandObject.Forbidden
            }
        }
        // 3. On the island you can see maps with a number: these tell you how
        // many steps are required, horizontally or vertically, to reach a
        // Treasure.
        for ((p, n2) in game.pos2hint) {
            fun f(): HintState {
                var possible = false
                next@ for (i in 0..<4) {
                    val os = BootyIslandGame.offset[i * 2]
                    var n1 = 1
                    var possible2 = false
                    var p2 = p + os
                    while (isValid(p2)) {
                        val o2 = this[p2]
                        if (o2 == BootyIslandObject.Treasure) {
                            if (n1 == n2) return HintState.Complete
                            continue@next
                        } else if (o2 == BootyIslandObject.Empty) {
                            if (n1 == n2) possible2 = true
                        } else if (n1 == n2)
                            continue@next
                        n1++
                        p2 += os
                    }
                    if (possible2) possible = true
                }
                return if (possible) HintState.Normal else HintState.Error
            }
            val s = f()
            pos2stateHint[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}
