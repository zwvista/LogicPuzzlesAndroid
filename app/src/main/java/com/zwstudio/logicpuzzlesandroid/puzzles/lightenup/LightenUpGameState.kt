package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LightenUpGameState(game: LightenUpGame) : CellsGameState<LightenUpGame, LightenUpGameMove, LightenUpGameState>(game) {
    var objArray = Array(rows * cols) { LightenUpObject() }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: LightenUpObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: LightenUpObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p].objType = LightenUpObjectType.Wall
    }

    override fun setObject(move: LightenUpGameMove): GameOperationType {
        var changed = false
        val p = move.p
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly

        fun adjustLightness(tolighten: Boolean) {
            fun f(n: Int) = if (tolighten) n + 1 else if (n > 0) n - 1 else n
            var obj = this[p]
            obj.lightness = f(obj.lightness)
            for (os in LightenUpGame.offset) {
                var p2 = p + os
                while (isValid(p2)) {
                    obj = this[p2]
                    // 5. Walls block light.
                    if (obj.objType == LightenUpObjectType.Wall) break
                    obj.lightness = f(obj.lightness)
                    p2 += os
                }
            }
            updateIsSolved()
        }

        fun objChanged() {
            changed = true
            this[p].objType = move.objType
        }

        val objOld = this[p].objType
        val objNew = move.objType
        if (objNew == LightenUpObjectType.Wall)
            this[p] = LightenUpObject(move.objType, 0)
        else if (objOld == LightenUpObjectType.Empty && objNew == LightenUpObjectType.Marker ||
            objOld == LightenUpObjectType.Marker && objNew == LightenUpObjectType.Empty)
            objChanged()
        else if (objOld == LightenUpObjectType.Empty && objNew == LightenUpObjectType.Lightbulb ||
            objOld == LightenUpObjectType.Marker && objNew == LightenUpObjectType.Lightbulb) {
            if (!(allowedObjectsOnly && this[p].lightness > 0)) {
                objChanged()
                adjustLightness(true)
            }
        } else if (objOld == LightenUpObjectType.Lightbulb && objNew == LightenUpObjectType.Empty ||
            objOld == LightenUpObjectType.Lightbulb && objNew == LightenUpObjectType.Marker) {
            objChanged()
            adjustLightness(false)
        }
        return if (changed) GameOperationType.MoveComplete else GameOperationType.Invalid
    }

    override fun switchObject(move: LightenUpGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        fun f(obj: LightenUpObjectType) = when (obj) {
            LightenUpObjectType.Empty -> if (markerOption == MarkerOptions.MarkerFirst) LightenUpObjectType.Marker else LightenUpObjectType.Lightbulb
            LightenUpObjectType.Lightbulb -> if (markerOption == MarkerOptions.MarkerLast) LightenUpObjectType.Marker else LightenUpObjectType.Empty
            LightenUpObjectType.Marker -> if (markerOption == MarkerOptions.MarkerFirst) LightenUpObjectType.Lightbulb else LightenUpObjectType.Empty
            else -> obj
        }
        val objOld = this[p].objType
        val objNew = f(objOld)
        if (objNew == LightenUpObjectType.Empty || objNew == LightenUpObjectType.Marker) {
            move.objType = objNew
            return setObject(move)
        }
        if (objNew == LightenUpObjectType.Lightbulb) {
            move.objType = if (allowedObjectsOnly && this[p].lightness > 0) f(objNew) else objNew
            return setObject(move)
        }
        return GameOperationType.Invalid
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
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                when (o.objType) {
                    LightenUpObjectType.Lightbulb -> {
                        // 4. A lightbulb can't light another lightbulb.
                        val s = if (o.lightness == 1) AllowedObjectState.Normal else AllowedObjectState.Error
                        pos2stateAllowed[p] = s
                        if (s == AllowedObjectState.Error) isSolved = false
                    }
                    LightenUpObjectType.Wall -> {
                        val n2 = game.pos2hint[p]!!
                        // 6. Walls without a number can have any number of lightbulbs.
                        if (n2 < 0) { pos2stateHint[p] = HintState.Normal; continue }
                        var n1 = 0
                        for (os in LightenUpGame.offset) {
                            val p2 = p + os
                            if (isValid(p2) && this[p2].objType == LightenUpObjectType.Lightbulb)
                                n1++
                        }
                        // 5. Walls with a number tell you how many lightbulbs
                        // are adjacent to it, horizontally and vertically.
                        val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
                        pos2stateHint[p] = s
                        if (s != HintState.Complete) isSolved = false
                    }
                    else ->
                        // 2. The goal is to put lightbulbs in the room so that all the blank(non-wall)
                        // squares are lit.
                        if (o.lightness == 0) isSolved = false
                }
            }
    }
}