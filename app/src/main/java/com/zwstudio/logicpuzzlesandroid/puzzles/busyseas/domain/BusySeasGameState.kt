package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class BusySeasGameState(game: BusySeasGame) : CellsGameState<BusySeasGame, BusySeasGameMove, BusySeasGameState>(game) {
    var objArray = Array<BusySeasObject>(rows() * cols()) { BusySeasEmptyObject() }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: BusySeasObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: BusySeasObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = BusySeasHintObject()
        updateIsSolved()
    }

    fun setObject(move: BusySeasGameMove): Boolean {
        if (this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: BusySeasGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is BusySeasEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) BusySeasMarkerObject() else BusySeasLighthouseObject()
            is BusySeasLighthouseObject -> if (markerOption == MarkerOptions.MarkerLast) BusySeasMarkerObject() else BusySeasEmptyObject()
            is BusySeasMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) BusySeasLighthouseObject() else BusySeasEmptyObject()
            else -> o
        }
        return setObject(move)
    }

    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val o = this[r, c]
                if (o is BusySeasLighthouseObject)
                    o.state = AllowedObjectState.Normal
                else if (o is BusySeasForbiddenObject)
                    this[r, c] = BusySeasEmptyObject()
            }
        for (r in 0 until rows())
            for (c in 0 until cols()) {
            val p = Position(r, c)
            fun hasLightedBoat(): Boolean {
                for (os in BusySeasGame.offset) {
                    val p2 = p.add(os)
                    while (isValid(p2)) {
                        if (this[p2] is BusySeasHintObject) return true
                        p2.addBy(os)
                    }
                }
                return false
            }
            val o = this[r, c]
            if (o is BusySeasLighthouseObject) {
                val s = if (o.state == AllowedObjectState.Normal && hasLightedBoat()) AllowedObjectState.Normal else AllowedObjectState.Error
                o.state = s
                if (s == AllowedObjectState.Error) isSolved = false
            }
        }
        // 3. A lighthouse lights all the tiles horizontally and vertically.
        for ((p, n2) in game.pos2hint) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0..3) {
                val os: Position = BusySeasGame.offset[i]
                val p2 = p.add(os)
                while (isValid(p2)) {
                    val o2 = this[p2]
                    // 3. A lighthouse's light is stopped by the first boat it meets.
                    if (o2 is BusySeasHintObject) continue@next
                    if (o2 is BusySeasEmptyObject)
                        rng.add(p2.plus())
                    else if (o2 is BusySeasLighthouseObject)
                        nums[i]++
                    p2.addBy(os)
                }
            }
            val n1 = nums.sum()
            // 2. Each boat has a number on it that tells you how many lighthouses are lighting it.
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete)
                isSolved = false
            else
                for (p2 in rng)
                    this[p2] = BusySeasForbiddenObject()
        }
    }
}
