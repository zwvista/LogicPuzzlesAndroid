package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.F0
import java.util.*

class LighthousesGameState(game: LighthousesGame) : CellsGameState<LighthousesGame, LighthousesGameMove, LighthousesGameState>(game) {
    var objArray: Array<LighthousesObject?>
    var pos2state = mutableMapOf<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: LighthousesObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: LighthousesObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: LighthousesGameMove): Boolean {
        if (get(move!!.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: LighthousesGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: LighthousesObject? ->
            if (obj is LighthousesEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) LighthousesMarkerObject() else LighthousesLighthouseObject()
            if (obj is LighthousesLighthouseObject) return@label if (markerOption == MarkerOptions.MarkerLast) LighthousesMarkerObject() else LighthousesEmptyObject()
            if (obj is LighthousesMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) LighthousesLighthouseObject() else LighthousesEmptyObject()
            obj
        }
        val o = get(move!!.p)
        move.obj = f.f(o)
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
        val allowedObjectsOnly = game!!.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val o = get(r, c)
            if (o is LighthousesLighthouseObject) o.state = AllowedObjectState.Normal else if (o is LighthousesForbiddenObject) set(r, c, LighthousesEmptyObject())
        }
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val hasNeighbor = label@ F0 {
                for (os in LighthousesGame.offset) {
                    val p2 = p.add(os)
                    if (!isValid(p2)) continue
                    val o2 = get(p2)
                    if (o2 is LighthousesHintObject || o2 is LighthousesLighthouseObject) return@label true
                }
                false
            }
            val o = get(r, c)
            if (o is LighthousesLighthouseObject) {
                // 4. Finally, no boat touches another boat or lighthouse, not even diagonally.
                // No lighthouse touches another lighthouse as well.
                val o2 = o
                o2.state = if (o2.state == AllowedObjectState.Normal && !hasNeighbor.f()) AllowedObjectState.Normal else AllowedObjectState.Error
            } else if ((o is LighthousesEmptyObject || o is LighthousesMarkerObject) &&
                allowedObjectsOnly && hasNeighbor.f()) // 4. Finally, no boat touches another boat or lighthouse, not even diagonally.
            // No lighthouse touches another lighthouse as well.
                set(r, c, LighthousesForbiddenObject())
        }
        for ((p, n2) in game!!.pos2hint) {
            val nums = intArrayOf(0, 0, 0, 0)
            val rng = mutableListOf<Position>()
            next@ for (i in 0..3) {
                val os: Position = LighthousesGame.offset.get(i * 2)
                val p2 = p!!.add(os)
                while (isValid(p2)) {
                    val o2 = get(p2)
                    if (o2 is LighthousesEmptyObject || o2 is LighthousesMarkerObject) rng.add(p2.plus()) else if (o2 is LighthousesLighthouseObject) nums[i]++
                    p2.addBy(os)
                }
            }
            val n1 = nums[0] + nums[1] + nums[2] + nums[3]
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
            if (allowedObjectsOnly && s != HintState.Normal) for (p2 in rng) set(p2, LighthousesForbiddenObject())
        }
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        Arrays.fill(objArray, LighthousesEmptyObject())
        for (p in game.pos2hint.keys) set(p, LighthousesHintObject())
        updateIsSolved()
    }
}