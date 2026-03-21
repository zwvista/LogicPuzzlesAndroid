package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class LighthousesGameState(game: LighthousesGame) : CellsGameState<LighthousesGame, LighthousesGameMove, LighthousesGameState>(game) {
    var objArray = Array(rows * cols) { LighthousesObject.Empty }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: LighthousesObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: LighthousesObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = LighthousesObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: LighthousesGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == LighthousesObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: LighthousesGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == LighthousesObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = get(move.p)) {
            LighthousesObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) LighthousesObject.Marker else LighthousesObject.Lighthouse
            LighthousesObject.Lighthouse -> if (markerOption == MarkerOptions.MarkerLast) LighthousesObject.Marker else LighthousesObject.Empty
            LighthousesObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) LighthousesObject.Lighthouse else LighthousesObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/Lighthouses

        Summary
        Lighten Up at Sea

        Description
        1. You are at sea and you need to find the lighthouses and light the boats.
        2. Each boat has a number on it that tells you how many lighthouses are lighting it.
        3. A lighthouse lights all the tiles horizontally and vertically and doesn't
           stop at boats or other lighthouses.
        4. Finally, no boat touches another boat or lighthouse, not even diagonally.
           No lighthouse touches another lighthouse as well.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == LighthousesObject.Lighthouse)
                    pos2stateAllowed[p] = AllowedObjectState.Normal
                else if (o == LighthousesObject.Forbidden)
                    this[p] = LighthousesObject.Empty
            }
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                fun hasNeighbor(): Boolean {
                    for (os in LighthousesGame.offset) {
                        val p2 = p + os
                        if (!isValid(p2)) continue
                        val o2 = this[p2]
                        if (o2 == LighthousesObject.Hint || o2 == LighthousesObject.Lighthouse) return true
                    }
                    return false
                }
                val o = this[p]
                if (o == LighthousesObject.Lighthouse)
                    // 4. Finally, no boat touches another boat or lighthouse, not even diagonally.
                    // No lighthouse touches another lighthouse as well.
                    pos2stateAllowed[p] = if (pos2stateAllowed[p] == AllowedObjectState.Normal && !hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o == LighthousesObject.Empty || o == LighthousesObject.Marker) && allowedObjectsOnly && hasNeighbor())
                    // 4. Finally, no boat touches another boat or lighthouse, not even diagonally.
                    // No lighthouse touches another lighthouse as well.
                    this[p] = LighthousesObject.Forbidden
            }
        for ((p, n2) in game.pos2hint) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0 until 4) {
                val os = LighthousesGame.offset[i * 2]
                var p2 = p + os
                while (isValid(p2)) {
                    val o2 = this[p2]
                    if (o2 == LighthousesObject.Empty || o2 == LighthousesObject.Marker)
                        rng.add(+p2)
                    else if (o2 == LighthousesObject.Lighthouse)
                        nums[i]++
                    p2 += os
                }
            }
            val n1 = nums[0] + nums[1] + nums[2] + nums[3]
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2stateHint[p] = s
            if (s != HintState.Complete) isSolved = false
            if (allowedObjectsOnly && s != HintState.Normal)
                for (p2 in rng)
                    this[p2] = LighthousesObject.Forbidden
        }
    }
}