package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class LighthousesGameState(game: LighthousesGame) : CellsGameState<LighthousesGame, LighthousesGameMove, LighthousesGameState>(game) {
    var objArray = Array<LighthousesObject>(rows * cols) { LighthousesEmptyObject() }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: LighthousesObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: LighthousesObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = LighthousesHintObject()
        updateIsSolved()
    }

    fun setObject(move: LighthousesGameMove): Boolean {
        if (this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: LighthousesGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = get(move.p)
        move.obj = when (o) {
            is LighthousesEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) LighthousesMarkerObject() else LighthousesLighthouseObject()
            is LighthousesLighthouseObject -> if (markerOption == MarkerOptions.MarkerLast) LighthousesMarkerObject() else LighthousesEmptyObject()
            is LighthousesMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) LighthousesLighthouseObject() else LighthousesEmptyObject()
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
                val o = this[r, c]
                if (o is LighthousesLighthouseObject)
                    o.state = AllowedObjectState.Normal
                else if (o is LighthousesForbiddenObject)
                    this[r, c] = LighthousesEmptyObject()
            }
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                fun hasNeighbor(): Boolean {
                    for (os in LighthousesGame.offset) {
                        val p2 = p + os
                        if (!isValid(p2)) continue
                        val o2 = get(p2)
                        if (o2 is LighthousesHintObject || o2 is LighthousesLighthouseObject) return true
                    }
                    return false
                }
                val o = this[r, c]
                if (o is LighthousesLighthouseObject)
                    // 4. Finally, no boat touches another boat or lighthouse, not even diagonally.
                    // No lighthouse touches another lighthouse as well.
                    o.state = if (o.state == AllowedObjectState.Normal && !hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is LighthousesEmptyObject || o is LighthousesMarkerObject) && allowedObjectsOnly && hasNeighbor())
                    // 4. Finally, no boat touches another boat or lighthouse, not even diagonally.
                    // No lighthouse touches another lighthouse as well.
                    this[r, c] = LighthousesForbiddenObject()
            }
        for ((p, n2) in game.pos2hint) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0 until 4) {
                val os: Position = LighthousesGame.offset[i * 2]
                var p2 = p + os
                while (isValid(p2)) {
                    val o2 = this[p2]
                    if (o2 is LighthousesEmptyObject || o2 is LighthousesMarkerObject)
                        rng.add(+p2)
                    else if (o2 is LighthousesLighthouseObject)
                        nums[i]++
                    p2 += os
                }
            }
            val n1 = nums[0] + nums[1] + nums[2] + nums[3]
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
            if (allowedObjectsOnly && s != HintState.Normal)
                for (p2 in rng)
                    this[p2] = LighthousesForbiddenObject()
        }
    }
}