package com.zwstudio.logicpuzzlesandroid.puzzles.funnynumbers

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import java.util.TreeMap

class FunnyNumbersGameState(game: FunnyNumbersGame) : CellsGameState<FunnyNumbersGame, FunnyNumbersGameMove, FunnyNumbersGameState>(game) {
    var objArray = Array<FunnyNumbersObject>(rows * cols) { FunnyNumbersObject.Empty }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: FunnyNumbersObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: FunnyNumbersObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FunnyNumbersGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FunnyNumbersGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is FunnyNumbersObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) FunnyNumbersObject.Marker else FunnyNumbersObject.Water()
            is FunnyNumbersObject.Water -> if (markerOption == MarkerOptions.MarkerLast) FunnyNumbersObject.Marker else FunnyNumbersObject.Empty
            is FunnyNumbersObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) FunnyNumbersObject.Water() else FunnyNumbersObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 2/Funny Numbers

        Summary
        Hahaha ... haha ... ehm ...

        Description
        1. Fill each region with numbers 1 to X where the X is the region area.
        2. Same numbers can't touch each other horizontally or vertically across regions.
        3. The numbers outside tell you the sum of the row or column.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is FunnyNumbersObject.Forbidden)
                    this[r, c] = FunnyNumbersObject.Empty
        // 2. You have to fill some water in it, considering that water pours down
        //    and levels itself like in reality.
        // 3. Areas of the same level which are horizontally connected will have
        //    the same water level.
        for (area in game.areas) {
            val row2rng = TreeMap(area.groupBy { it.row })
            val rowNotFilled = row2rng.keys.reversed().firstOrNull {
                row2rng[it]!!.any { this[it] !is FunnyNumbersObject.Water }
            } ?: continue
            val rng = area.filter { this[it] is FunnyNumbersObject.Water }
            val rngError = rng.filter { it.row < rowNotFilled }
            rng.forEach { this[it] = FunnyNumbersObject.Water() }
            if (rngError.isEmpty()) continue
            isSolved = false
            rngError.forEach { this[it] = FunnyNumbersObject.Water(state = AllowedObjectState.Error) }
        }
        // 4. The numbers on the border show you how many tiles of each row and
        //    column are filled.
        for (r in 0 until rows) {
            val n2 = game.row2hint[r]
            if (n2 == FunnyNumbersGame.PUZ_UNKNOWN) continue
            val n1 = (0 until cols).count { this[r, it] is FunnyNumbersObject.Water }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly)
                (0 until cols).filter { this[r, it] is FunnyNumbersObject.Empty }.forEach {
                    this[r, it] = FunnyNumbersObject.Forbidden
                }
        }
        for (c in 0 until cols) {
            val n2 = game.col2hint[c]
            if (n2 == FunnyNumbersGame.PUZ_UNKNOWN) continue
            val n1 = (0 until rows).count { this[it, c] is FunnyNumbersObject.Water }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly)
                (0 until rows).filter { this[it, c] is FunnyNumbersObject.Empty }.forEach {
                    this[it, c] = FunnyNumbersObject.Forbidden
                }
        }
    }
}