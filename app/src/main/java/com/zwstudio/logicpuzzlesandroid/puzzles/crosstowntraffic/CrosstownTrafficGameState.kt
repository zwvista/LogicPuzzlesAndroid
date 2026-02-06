package com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CrosstownTrafficGameState(game: CrosstownTrafficGame) : CellsGameState<CrosstownTrafficGame, CrosstownTrafficGameMove, CrosstownTrafficGameState>(game) {
    val objArray: Array<CrosstownTrafficObject>

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: CrosstownTrafficObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: CrosstownTrafficObject) {this[p.row, p.col] = obj}

    init {
        objArray = Array<CrosstownTrafficObject>(rows * cols) { CrosstownTrafficEmptyObject }
        for ((p, n) in game.pos2hint) this[p] = CrosstownTrafficHintObject()
        updateIsSolved()
    }

    override fun setObject(move: CrosstownTrafficGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p].toString() == move.obj.toString()) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: CrosstownTrafficGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            is CrosstownTrafficEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) CrosstownTrafficMarkerObject else CrosstownTrafficPebbleObject
            is CrosstownTrafficPebbleObject -> CrosstownTrafficGemObject()
            is CrosstownTrafficGemObject -> if (markerOption == MarkerOptions.MarkerLast) CrosstownTrafficMarkerObject else CrosstownTrafficEmptyObject
            is CrosstownTrafficMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) CrosstownTrafficPebbleObject else CrosstownTrafficEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 3/Crosstown Traffic

        Summary
        looks like pipes made of asphalt

        Description
        1. Draw a circuit (looping road)
        2. The road may cross itself, but otherwise does not touch or retrace itself.
        3. The numbers along the edge indicate the stretch of the nearest section
           of road from that point, in corresponding row or column.
        4. For example if the first stretch of road is curve, straight and curve
           the number of that hint is 3.
        5. Another example: if the first stretch of road is curve and curve,
           the number of that hint is 2.
        6. Not all tiles should be used. In some levels some part of the board
           can remain unused.
    */
    private fun updateIsSolved() {
        isSolved = true
        fun f(r: Int, c: Int): Boolean {
            val o = this[r, c]
            return o is CrosstownTrafficGemObject || o is CrosstownTrafficPebbleObject
        }
        for (r in 1 until rows - 1) {
            val (p1, p2) = Position(r, 0) to Position(r, cols - 1)
            val (h1, h2) = game.pos2hint[p1]!! to game.pos2hint[p2]!!
            val gems = (1 until cols - 1).map { Position(r, it) }.filter { this[it] is CrosstownTrafficGemObject }
            if (gems.size == 1) {
                // 1. The board contains one Sapphire (Blue Gem) on each row and column.
                val p = gems.first()
                val c = p.col
                (this[p] as CrosstownTrafficGemObject).state = AllowedObjectState.Normal
                // 2. There are also a random amount of Pebbles (in White) on the board.
                // 3. A number on the border tells you how many stones you can see from
                //    there, up to and including the Sapphire.
                // 4. The Sapphire (blue) hide the Pebbles (white) behind them.
                val (n1, n2) = (1..c).count { f(r, it) } to (c until cols - 1).count { f(r, it) }
                val s1 = if (n1 < h1) HintState.Normal else if (n1 == h1) HintState.Complete else HintState.Error
                val s2 = if (n2 < h2) HintState.Normal else if (n2 == h2) HintState.Complete else HintState.Error
                (this[p1] as CrosstownTrafficHintObject).state = s1; (this[p2] as CrosstownTrafficHintObject).state = s2
                if (s1 != HintState.Complete || s2 != HintState.Complete) { isSolved = false }
            } else {
                isSolved = false
                gems.forEach { (this[it] as CrosstownTrafficGemObject).state = AllowedObjectState.Normal }
            }
        }
        for (c in 1 until cols - 1) {
            val (p1, p2) = Position(0, c) to Position(rows - 1, c)
            val (h1, h2) = game.pos2hint[p1]!! to game.pos2hint[p2]!!
            val gems = (1 until rows - 1).map { Position(it, c) }.filter { this[it] is CrosstownTrafficGemObject }
            if (gems.size == 1) {
                // 1. The board contains one Sapphire (Blue Gem) on each row and column.
                val p = gems.first()
                val r = p.row
                (this[p] as CrosstownTrafficGemObject).state = AllowedObjectState.Normal
                // 2. There are also a random amount of Pebbles (in White) on the board.
                // 3. A number on the border tells you how many stones you can see from
                //    there, up to and including the Sapphire.
                // 4. The Sapphire (blue) hide the Pebbles (white) behind them.
                val (n1, n2) = (1..r).count { f(it, c) } to (r until rows - 1).count { f(it, c) }
                val s1 = if (n1 < h1) HintState.Normal else if (n1 == h1) HintState.Complete else HintState.Error
                val s2 = if (n2 < h2) HintState.Normal else if (n2 == h2) HintState.Complete else HintState.Error
                (this[p1] as CrosstownTrafficHintObject).state = s1; (this[p2] as CrosstownTrafficHintObject).state = s2
                if (s1 != HintState.Complete || s2 != HintState.Complete) { isSolved = false }
            } else {
                isSolved = false
                gems.forEach { (this[it] as CrosstownTrafficGemObject).state = AllowedObjectState.Normal }
            }
        }
    }
}