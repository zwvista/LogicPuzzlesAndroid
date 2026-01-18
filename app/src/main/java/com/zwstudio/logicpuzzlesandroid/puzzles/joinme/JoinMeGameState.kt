package com.zwstudio.logicpuzzlesandroid.puzzles.joinme

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import java.util.TreeMap

class JoinMeGameState(game: JoinMeGame) : CellsGameState<JoinMeGame, JoinMeGameMove, JoinMeGameState>(game) {
    var objArray = Array<JoinMeObject>(rows * cols) { JoinMeEmptyObject }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: JoinMeObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: JoinMeObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: JoinMeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: JoinMeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is JoinMeEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) JoinMeMarkerObject else JoinMeWaterObject()
            is JoinMeWaterObject -> if (markerOption == MarkerOptions.MarkerLast) JoinMeMarkerObject else JoinMeEmptyObject
            is JoinMeMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) JoinMeWaterObject() else JoinMeEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 2/Join Me!

        Summary
        Communicating Vessels

        Description
        1. Connect the different patches with one stitch (more in later levels).
        2. The numbers on the outside tell you how many stitches you can see from
           there in the row/column.
        3. A cell can contain only one stitch.
        4. Later levels will show you in the top right how many stitches you have
           to put between patches.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is JoinMeForbiddenObject)
                    this[r, c] = JoinMeEmptyObject
        // 2. You have to fill some water in it, considering that water pours down
        //    and levels itself like in reality.
        // 3. Areas of the same level which are horizontally connected will have
        //    the same water level.
        for (area in game.areas) {
            val row2rng = TreeMap(area.groupBy { it.row })
            val rowNotFilled = row2rng.keys.reversed().firstOrNull {
                row2rng[it]!!.any { this[it] !is JoinMeWaterObject }
            } ?: continue
            val rng = area.filter { this[it] is JoinMeWaterObject }
            val rngError = rng.filter { it.row < rowNotFilled }
            rng.forEach { this[it] = JoinMeWaterObject() }
            if (rngError.isEmpty()) continue
            isSolved = false
            rngError.forEach { this[it] = JoinMeWaterObject(state = AllowedObjectState.Error) }
        }
        // 4. The numbers on the border show you how many tiles of each row and
        //    column are filled.
        for (r in 0 until rows) {
            val n2 = game.row2hint[r]
            if (n2 == JoinMeGame.PUZ_UNKNOWN) continue
            val n1 = (0 until cols).count { this[r, it] is JoinMeWaterObject }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly)
                (0 until cols).filter { this[r, it] is JoinMeEmptyObject }.forEach {
                    this[r, it] = JoinMeForbiddenObject
                }
        }
        for (c in 0 until cols) {
            val n2 = game.col2hint[c]
            if (n2 == JoinMeGame.PUZ_UNKNOWN) continue
            val n1 = (0 until rows).count { this[it, c] is JoinMeWaterObject }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly)
                (0 until rows).filter { this[it, c] is JoinMeEmptyObject }.forEach {
                    this[it, c] = JoinMeForbiddenObject
                }
        }
    }
}