package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F

class LightenUpGameState(game: LightenUpGame) : CellsGameState<LightenUpGame, LightenUpGameMove, LightenUpGameState>(game) {
    var objArray: Array<LightenUpObject?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: LightenUpObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: LightenUpObject?) {
        set(p!!.row, p.col, obj)
    }

    private fun objChanged(move: LightenUpGameMove?, toajust: Boolean, tolighten: Boolean): Boolean {
        val p = move!!.p
        set(p, move.obj)
        if (toajust) {
            val f = F { n: Int -> if (tolighten) n + 1 else if (n > 0) n - 1 else n }
            var obj = get(p)
            obj!!.lightness = f.f(obj.lightness)
            for (os in LightenUpGame.Companion.offset) {
                val p2 = p!!.add(os)
                while (isValid(p2)) {
                    obj = get(p2)
                    if (obj is LightenUpWallObject) break
                    obj!!.lightness = f.f(obj.lightness)
                    p2.addBy(os)
                }
            }
            updateIsSolved()
        }
        return true
    }

    fun setObject(move: LightenUpGameMove): Boolean {
        val p = move!!.p
        val objOld = get(p)
        val objNew = move.obj
        objNew!!.lightness = objOld!!.lightness
        if (objOld is LightenUpEmptyObject && objNew is LightenUpMarkerObject ||
            objOld is LightenUpMarkerObject && objNew is LightenUpEmptyObject) return objChanged(move, false, false)
        if (objOld is LightenUpEmptyObject && objNew is LightenUpLightbulbObject ||
            objOld is LightenUpMarkerObject && objNew is LightenUpLightbulbObject) return objChanged(move, true, true)
        if (objOld is LightenUpLightbulbObject && objNew is LightenUpEmptyObject ||
            objOld is LightenUpLightbulbObject && objNew is LightenUpMarkerObject) return objChanged(move, true, false)
        (objNew as? LightenUpWallObject)?.let { set(p, it) }
        return false
    }

    fun switchObject(move: LightenUpGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val allowedObjectsOnly = game!!.gdi.isAllowedObjectsOnly
        val f = label@ F { obj: LightenUpObject? ->
            if (obj is LightenUpEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) LightenUpMarkerObject() else LightenUpLightbulbObject()
            if (obj is LightenUpLightbulbObject) return@label if (markerOption == MarkerOptions.MarkerLast) LightenUpMarkerObject() else LightenUpEmptyObject()
            if (obj is LightenUpMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) LightenUpLightbulbObject() else LightenUpEmptyObject()
            obj
        }
        val objOld = get(move!!.p)
        val objNew = f.f(objOld)
        if (objNew is LightenUpEmptyObject || objNew is LightenUpMarkerObject) {
            move.obj = objNew
            return setObject(move)
        }
        if (objNew is LightenUpLightbulbObject) {
            move.obj = if (allowedObjectsOnly && objOld!!.lightness > 0) f.f(objNew) else objNew
            return setObject(move)
        }
        return false
    }

    /*
        iOS Game: Logic Games/Puzzle Set 2/Lighten Up

        Summary
        Place lightbulbs to light up all the room squares

        Description
        1. What you see from above is a room and the marked squares are walls.
        2. The goal is to put lightbulbs in the room so that all the blank(non-wall)
           squares are lit, following these rules.
        3. Lightbulbs light all free, unblocked squares horizontally and vertically.
        4. A lightbulb can't light another lightbulb.
        5. Walls block light. Also walls with a number tell you how many lightbulbs
           are adjacent to it, horizontally and vertically.
        6. Walls without a number can have any number of lightbulbs. However,
           lightbulbs don't need to be adjacent to a wall.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o = get(r, c)
            if (o is LightenUpEmptyObject && o.lightness == 0 ||
                o is LightenUpMarkerObject && o.lightness == 0) isSolved = false else if (o is LightenUpLightbulbObject) {
                o.state = if (o.lightness == 1) AllowedObjectState.Normal else AllowedObjectState.Error
                if (o.lightness > 1) isSolved = false
            } else if (o is LightenUpWallObject) {
                val n2 = game!!.pos2hint[p]!!
                if (n2 < 0) continue
                var n1 = 0
                for (os in LightenUpGame.Companion.offset) {
                    val p2 = p.add(os)
                    if (!isValid(p2)) continue
                    if (get(p2) is LightenUpLightbulbObject) n1++
                }
                o.state = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
                if (n1 != n2) isSolved = false
            }
        }
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        for (i in objArray.indices) objArray[i] = LightenUpEmptyObject()
        for ((p, n) in game.pos2hint) {
            val o = LightenUpWallObject()
            o.state = if (n <= 0) HintState.Complete else HintState.Normal
            set(p, o)
        }
    }
}