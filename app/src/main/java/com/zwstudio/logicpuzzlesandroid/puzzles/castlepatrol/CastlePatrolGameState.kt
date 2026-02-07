package com.zwstudio.logicpuzzlesandroid.puzzles.castlepatrol

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CastlePatrolGameState(game: CastlePatrolGame) : CellsGameState<CastlePatrolGame, CastlePatrolGameMove, CastlePatrolGameState>(game) {
    var objArray = Array<CastlePatrolObject>(rows * cols) { CastlePatrolEmptyObject() }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: CastlePatrolObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: CastlePatrolObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = CastlePatrolWallObject(if (n <= 0) HintState.Complete else HintState.Normal)
    }

    private fun objChanged(move: CastlePatrolGameMove, toajust: Boolean, tolighten: Boolean): GameOperationType {
        val p = move.p
        this[p] = move.obj
        if (toajust) {
            fun f(n: Int) = if (tolighten) n + 1 else if (n > 0) n - 1 else n
            var obj = this[p]
            obj.lightness = f(obj.lightness)
            for (os in CastlePatrolGame.offset) {
                var p2 = p + os
                while (isValid(p2)) {
                    obj = this[p2]
                    if (obj is CastlePatrolWallObject) break
                    obj.lightness = f(obj.lightness)
                    p2 += os
                }
            }
            updateIsSolved()
        }
        return GameOperationType.MoveComplete
    }

    override fun setObject(move: CastlePatrolGameMove): GameOperationType {
        val p = move.p
        val objOld = this[p]
        val objNew = move.obj
        objNew.lightness = objOld.lightness
        if (objOld is CastlePatrolEmptyObject && objNew is CastlePatrolMarkerObject ||
            objOld is CastlePatrolMarkerObject && objNew is CastlePatrolEmptyObject) return objChanged(move, false, false)
        if (objOld is CastlePatrolEmptyObject && objNew is CastlePatrolLightbulbObject ||
            objOld is CastlePatrolMarkerObject && objNew is CastlePatrolLightbulbObject) return objChanged(move, true, true)
        if (objOld is CastlePatrolLightbulbObject && objNew is CastlePatrolEmptyObject ||
            objOld is CastlePatrolLightbulbObject && objNew is CastlePatrolMarkerObject) return objChanged(move, true, false)
        this[p] = CastlePatrolWallObject()
        return GameOperationType.Invalid
    }

    override fun switchObject(move: CastlePatrolGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        fun f(obj: CastlePatrolObject) = when (obj) {
            is CastlePatrolEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) CastlePatrolMarkerObject() else CastlePatrolLightbulbObject()
            is CastlePatrolLightbulbObject -> if (markerOption == MarkerOptions.MarkerLast) CastlePatrolMarkerObject() else CastlePatrolEmptyObject()
            is CastlePatrolMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) CastlePatrolLightbulbObject() else CastlePatrolEmptyObject()
            else -> obj
        }
        val objOld = this[move.p]
        val objNew = f(objOld)
        if (objNew is CastlePatrolEmptyObject || objNew is CastlePatrolMarkerObject) {
            move.obj = objNew
            return setObject(move)
        }
        if (objNew is CastlePatrolLightbulbObject) {
            move.obj = if (allowedObjectsOnly && objOld.lightness > 0) f(objNew) else objNew
            return setObject(move)
        }
        return GameOperationType.Invalid
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 4/Castle Patrol

        Summary
        Don't fall down the wall

        Description
        1. Divide the grid into walls and empty areas. Every area contains one number.
        2. The number indicates the size of the area. Numbers in wall tiles are part
           of wall areas; numbers in empty tiles are part of empty areas.
        3. Areas of the same type cannot share an edge.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[r, c]
                if (o is CastlePatrolEmptyObject && o.lightness == 0 || o is CastlePatrolMarkerObject && o.lightness == 0)
                    isSolved = false
                else if (o is CastlePatrolLightbulbObject) {
                    o.state = if (o.lightness == 1) AllowedObjectState.Normal else AllowedObjectState.Error
                    if (o.lightness > 1) isSolved = false
                } else if (o is CastlePatrolWallObject) {
                    val n2 = game.pos2hint[p]!!
                    if (n2 < 0) continue
                    var n1 = 0
                    for (os in CastlePatrolGame.offset) {
                        val p2 = p + os
                        if (!isValid(p2)) continue
                        if (this[p2] is CastlePatrolLightbulbObject) n1++
                    }
                    o.state = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
                    if (n1 != n2) isSolved = false
                }
            }
    }
}