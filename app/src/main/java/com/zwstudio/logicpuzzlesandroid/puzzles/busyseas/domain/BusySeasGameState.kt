package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.F0
import java.util.*

class BusySeasGameState(game: BusySeasGame) : CellsGameState<BusySeasGame?, BusySeasGameMove?, BusySeasGameState?>(game) {
    var objArray: Array<BusySeasObject?>
    var pos2state = mutableMapOf<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: BusySeasObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: BusySeasObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: BusySeasGameMove?): Boolean {
        if (get(move!!.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: BusySeasGameMove?): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: BusySeasObject? ->
            if (obj is BusySeasEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) BusySeasMarkerObject() else BusySeasLighthouseObject()
            if (obj is BusySeasLighthouseObject) return@label if (markerOption == MarkerOptions.MarkerLast) BusySeasMarkerObject() else BusySeasEmptyObject()
            if (obj is BusySeasMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) BusySeasLighthouseObject() else BusySeasEmptyObject()
            obj
        }
        val o = get(move!!.p)
        move.obj = f.f(o)
        return setObject(move)
    }

    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val o = get(r, c)
            if (o is BusySeasLighthouseObject) o.state = AllowedObjectState.Normal else if (o is BusySeasForbiddenObject) set(r, c, BusySeasEmptyObject())
        }
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val hasLightedBoat = label@ F0 {
                for (os in BusySeasGame.Companion.offset) {
                    val p2 = p.add(os)
                    while (isValid(p2)) {
                        if (isValid(p2) && get(p2) is BusySeasHintObject) return@label true
                        p2.addBy(os)
                    }
                }
                false
            }
            val o = get(r, c)
            if (o is BusySeasLighthouseObject) {
                val o2 = o
                val s = if (o2.state == AllowedObjectState.Normal && hasLightedBoat.f()) AllowedObjectState.Normal else AllowedObjectState.Error
                o2.state = s
                if (s == AllowedObjectState.Error) isSolved = false
            }
        }
        // 3. A lighthouse lights all the tiles horizontally and vertically.
        for ((p, n2) in game!!.pos2hint) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0..3) {
                val os: Position = BusySeasGame.Companion.offset.get(i)
                val p2 = p!!.add(os)
                while (isValid(p2)) {
                    val o2 = get(p2)
                    // 3. A lighthouse's light is stopped by the first boat it meets.
                    if (o2 is BusySeasHintObject) continue@next
                    if (o2 is BusySeasEmptyObject) rng.add(p2.plus()) else if (o2 is BusySeasLighthouseObject) nums[i]++
                    p2.addBy(os)
                }
            }
            val n1 = nums[0] + nums[1] + nums[2] + nums[3]
            // 2. Each boat has a number on it that tells you how many lighthouses are lighting it.
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false else for (p2 in rng) set(p2, BusySeasForbiddenObject())
        }
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        Arrays.fill(objArray, BusySeasEmptyObject())
        for (p in game.pos2hint.keys) set(p, BusySeasHintObject())
        updateIsSolved()
    }
}