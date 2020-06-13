package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class BootyIslandGameState(game: BootyIslandGame) : CellsGameState<BootyIslandGame, BootyIslandGameMove, BootyIslandGameState>(game) {
    var objArray = Array<BootyIslandObject>(rows * 2) { BootyIslandEmptyObject() }
    var pos2state = mutableMapOf<Position, HintState>()

    init {
        for (p in game.pos2hint.keys)
            this[p] = BootyIslandHintObject()
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: BootyIslandObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: BootyIslandObject) {this[p.row, p.col] = obj}

    fun setObject(move: BootyIslandGameMove): Boolean {
        val p = move.p
        val objOld = this[p]
        val objNew = move.obj
        if (objOld is BootyIslandHintObject || objOld == objNew)
            return false
        this[p] = objNew
        updateIsSolved()
        return true
    }

    fun switchObject(move: BootyIslandGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is BootyIslandEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) BootyIslandMarkerObject() else BootyIslandTreasureObject()
            is BootyIslandTreasureObject -> if (markerOption == MarkerOptions.MarkerLast) BootyIslandMarkerObject() else BootyIslandEmptyObject()
            is BootyIslandMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) BootyIslandTreasureObject() else BootyIslandEmptyObject()
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
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val o = this[r, c]
                if (o is BootyIslandForbiddenObject)
                    this[r, c] = BootyIslandEmptyObject()
                else if (o is BootyIslandTreasureObject)
                    o.state = AllowedObjectState.Normal
            }
        // 4. Pirates don't bury their Treasures touching each other, even diagonally.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                fun hasNeighbor(): Boolean {
                    for (os in BootyIslandGame.offset) {
                        val p2 = p + os
                        if (isValid(p2) && this[p2] is BootyIslandTreasureObject)
                            return true
                    }
                    return false
                }
                val o = this[r, c]
                if (o is BootyIslandTreasureObject) {
                    val s = if (o.state == AllowedObjectState.Normal && !hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                    o.state = s
                    if (s == AllowedObjectState.Error) isSolved = false
                } else if ((o is BootyIslandEmptyObject || o is BootyIslandMarkerObject) && allowedObjectsOnly && hasNeighbor())
                    this[r, c] = BootyIslandForbiddenObject()
            }
        // 2. In fact there's only one Treasure for each row.
        for (r in 0 until rows) {
            var n1 = 0
            val n2 = 1
            for (c in 0 until cols)
                if (this[r, c] is BootyIslandTreasureObject)
                    n1++
            if (n1 != n2) isSolved = false
            for (c in 0 until cols) {
                val o = this[r, c]
                if (o is BootyIslandTreasureObject)
                    o.state = if (o.state == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is BootyIslandEmptyObject || o is BootyIslandMarkerObject) && n1 == n2 && allowedObjectsOnly)
                    this[r, c] = BootyIslandForbiddenObject()
            }
        }
        // 2. In fact there's only one Treasure for each column.
        for (c in 0 until cols) {
            var n1 = 0
            val n2 = 1
            for (r in 0 until rows)
                if (this[r, c] is BootyIslandTreasureObject)
                    n1++
            if (n1 != n2) isSolved = false
            for (r in 0 until rows) {
                val o = this[r, c]
                if (o is BootyIslandTreasureObject)
                    o.state = if (o.state == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is BootyIslandEmptyObject || o is BootyIslandMarkerObject) && n1 == n2 && allowedObjectsOnly)
                    this[r, c] = BootyIslandForbiddenObject()
            }
        }
        // 3. On the island you can see maps with a number: these tell you how
        // many steps are required, horizontally or vertically, to reach a
        // Treasure.
        for ((p, n2) in game.pos2hint) {
            fun f(): HintState {
                var possible = false
                next@ for (i in 0 until 4) {
                    val os = BootyIslandGame.offset[i * 2]
                    var n1 = 1
                    var possible2 = false
                    var p2 = p + os
                    while (isValid(p2)) {
                        val o2 = this[p2]
                        if (o2 is BootyIslandTreasureObject) {
                            if (n1 == n2) return HintState.Complete
                            continue@next
                        } else if (o2 is BootyIslandEmptyObject) {
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
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}
