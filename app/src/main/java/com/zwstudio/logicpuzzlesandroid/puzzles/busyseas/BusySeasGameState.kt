package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class BusySeasGameState(game: BusySeasGame) : CellsGameState<BusySeasGame, BusySeasGameMove, BusySeasGameState>(game) {
    var objArray = Array(rows * cols) { BusySeasObject.Empty }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: BusySeasObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: BusySeasObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = BusySeasObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: BusySeasGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == BusySeasObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: BusySeasGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == BusySeasObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            BusySeasObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) BusySeasObject.Marker else BusySeasObject.Lighthouse
            BusySeasObject.Lighthouse -> if (markerOption == MarkerOptions.MarkerLast) BusySeasObject.Marker else BusySeasObject.Empty
            BusySeasObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) BusySeasObject.Lighthouse else BusySeasObject.Empty
            else -> o
        }
        return setObject(move)
    }

    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == BusySeasObject.Lighthouse)
                    pos2stateAllowed[p] = AllowedObjectState.Normal
                else if (o == BusySeasObject.Forbidden)
                    this[p] = BusySeasObject.Empty
            }
        for (r in 0 until rows)
            for (c in 0 until cols) {
            val p = Position(r, c)
            fun hasLightedBoat(): Boolean {
                for (os in BusySeasGame.offset) {
                    var p2 = p + os
                    while (isValid(p2)) {
                        if (this[p2] == BusySeasObject.Hint) return true
                        p2 += os
                    }
                }
                return false
            }
            val o = this[p]
            if (o == BusySeasObject.Lighthouse) {
                val s = if (pos2stateAllowed[p] == AllowedObjectState.Normal && hasLightedBoat()) AllowedObjectState.Normal else AllowedObjectState.Error
                pos2stateAllowed[p] = s
                if (s == AllowedObjectState.Error) isSolved = false
            }
        }
        // 3. A lighthouse lights all the tiles horizontally and vertically.
        for ((p, n2) in game.pos2hint) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0 until 4) {
                val os = BusySeasGame.offset[i]
                var p2 = p + os
                while (isValid(p2)) {
                    val o2 = this[p2]
                    // 3. A lighthouse's light is stopped by the first boat it meets.
                    if (o2 == BusySeasObject.Hint) continue@next
                    if (o2 == BusySeasObject.Empty)
                        rng.add(+p2)
                    else if (o2 == BusySeasObject.Lighthouse)
                        nums[i]++
                    p2 += os
                }
            }
            val n1 = nums.sum()
            // 2. Each boat has a number on it that tells you how many lighthouses are lighting it.
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2stateHint[p] = s
            if (s != HintState.Complete)
                isSolved = false
            else
                for (p2 in rng)
                    this[p2] = BusySeasObject.Forbidden
        }
    }
}
