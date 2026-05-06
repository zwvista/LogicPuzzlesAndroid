package com.zwstudio.logicpuzzlesandroid.puzzles.gems

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class GemsGameState(game: GemsGame) : CellsGameState<GemsGame, GemsGameMove, GemsGameState>(game) {
    val objArray = Array(rows * cols) { GemsObject.Empty }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: GemsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: GemsObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = GemsObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: GemsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p].toString() == move.obj.toString()) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: GemsGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            GemsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GemsObject.Marker else GemsObject.Pebble
            GemsObject.Pebble -> GemsObject.Gem
            GemsObject.Gem -> if (markerOption == MarkerOptions.MarkerLast) GemsObject.Marker else GemsObject.Empty
            GemsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GemsObject.Pebble else GemsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 1/Gems

        Summary
        Gemscraper

        Description
        1. The board contains one Sapphire (Blue Gem) on each row and column.
        2. There are also a random amount of Pebbles (in White) on the board.
        3. A number on the border tells you how many stones you can see from
           there, up to and including the Sapphire.
        4. The Sapphire (blue) hide the Pebbles (white) behind them.
    */
    private fun updateIsSolved() {
        isSolved = true
        fun f(r: Int, c: Int): Boolean {
            val o = this[r, c]
            return o == GemsObject.Gem || o == GemsObject.Pebble
        }
        for (r in 1..<rows - 1) {
            val (p1, p2) = Position(r, 0) to Position(r, cols - 1)
            val (h1, h2) = game.pos2hint[p1]!! to game.pos2hint[p2]!!
            val gems = (1..<cols - 1).map { Position(r, it) }.filter { this[it] == GemsObject.Gem }
            if (gems.size == 1) {
                // 1. The board contains one Sapphire (Blue Gem) on each row and column.
                val p = gems.first()
                val c = p.col
                pos2stateAllowed[p] = AllowedObjectState.Normal
                // 2. There are also a random amount of Pebbles (in White) on the board.
                // 3. A number on the border tells you how many stones you can see from
                //    there, up to and including the Sapphire.
                // 4. The Sapphire (blue) hide the Pebbles (white) behind them.
                val (n1, n2) = (1..c).count { f(r, it) } to (c..<cols - 1).count { f(r, it) }
                val s1 = if (n1 < h1) HintState.Normal else if (n1 == h1) HintState.Complete else HintState.Error
                val s2 = if (n2 < h2) HintState.Normal else if (n2 == h2) HintState.Complete else HintState.Error
                pos2stateHint[p1] = s1; pos2stateHint[p2] = s2
                if (s1 != HintState.Complete || s2 != HintState.Complete) { isSolved = false }
            } else {
                isSolved = false
                gems.forEach { pos2stateAllowed[it] = AllowedObjectState.Normal }
            }
        }
        for (c in 1..<cols - 1) {
            val (p1, p2) = Position(0, c) to Position(rows - 1, c)
            val (h1, h2) = game.pos2hint[p1]!! to game.pos2hint[p2]!!
            val gems = (1..<rows - 1).map { Position(it, c) }.filter { this[it] == GemsObject.Gem }
            if (gems.size == 1) {
                // 1. The board contains one Sapphire (Blue Gem) on each row and column.
                val p = gems.first()
                val r = p.row
                pos2stateAllowed[p] = AllowedObjectState.Normal
                // 2. There are also a random amount of Pebbles (in White) on the board.
                // 3. A number on the border tells you how many stones you can see from
                //    there, up to and including the Sapphire.
                // 4. The Sapphire (blue) hide the Pebbles (white) behind them.
                val (n1, n2) = (1..r).count { f(it, c) } to (r..<rows - 1).count { f(it, c) }
                val s1 = if (n1 < h1) HintState.Normal else if (n1 == h1) HintState.Complete else HintState.Error
                val s2 = if (n2 < h2) HintState.Normal else if (n2 == h2) HintState.Complete else HintState.Error
                pos2stateHint[p1] = s1; pos2stateHint[p2] = s2
                if (s1 != HintState.Complete || s2 != HintState.Complete) { isSolved = false }
            } else {
                isSolved = false
                gems.forEach { pos2stateAllowed[it] = AllowedObjectState.Normal }
            }
        }
    }
}