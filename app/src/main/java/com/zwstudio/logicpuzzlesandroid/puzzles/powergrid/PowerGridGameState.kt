package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import kotlin.math.abs

class PowerGridGameState(game: PowerGridGame) : CellsGameState<PowerGridGame, PowerGridGameMove, PowerGridGameState>(game) {
    var objArray = Array<PowerGridObject>(rows * cols) { PowerGridObject.Empty }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: PowerGridObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: PowerGridObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: PowerGridGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PowerGridGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            PowerGridObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) PowerGridObject.Marker else PowerGridObject.Post
            PowerGridObject.Post -> if (markerOption == MarkerOptions.MarkerLast) PowerGridObject.Marker else PowerGridObject.Empty
            PowerGridObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) PowerGridObject.Post else PowerGridObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 14/Power Grid

        Summary
        Utility Posts

        Description
        1. Your task is to identify Utility Posts of a Power Grid.
        2. There are two Posts in each Row and in each Column.
        3. The numbers on the side tell you the length of the cables between
           the two Posts (in that Row or Column).
        4. Or in other words, the number of empty tiles between two Posts.
        5. Posts cannot touch themselves, not even diagonally.
        6. Posts don't have to form a single connected chain.

        Variant
        7. On some levels, there are exactly two Posts in each diagonal too.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == PowerGridObject.Forbidden)
                    this[p] = PowerGridObject.Empty
                else if (o == PowerGridObject.Post)
                    pos2state[p] = AllowedObjectState.Normal
            }
        for (r in 0..<rows) {
            val posts = mutableListOf<Position>()
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p] == PowerGridObject.Post)
                    posts.add(p)
            }
            val n1 = posts.size
            val n2 = game.row2hint[r] + 1
            // 2. There are two Posts in each Row.
            // 3. The numbers on the side tell you the length of the cables between
            // the two Posts (in that Row).
            val s = if (n1 < 2) HintState.Normal else if (n1 == 2 && (n2 == PowerGridGame.PUZ_UNKNOWN + 1 || n2 == posts[1].col - posts[0].col)) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
            if (s == HintState.Error)
                for (p in posts)
                    pos2state[p] = AllowedObjectState.Error
            if (allowedObjectsOnly && n1 > 0)
                for (c in 0..<cols)
                    if (this[r, c] == PowerGridObject.Empty && (n1 > 1 || n2 != PowerGridGame.PUZ_UNKNOWN + 1 && n2 != abs(posts[0].col - c)))
                        this[r, c] = PowerGridObject.Forbidden
        }
        for (c in 0..<cols) {
            val posts = mutableListOf<Position>()
            for (r in 0..<rows) {
                val p = Position(r, c)
                if (this[p] == PowerGridObject.Post)
                    posts.add(p)
            }
            val n1 = posts.size
            val n2 = game.col2hint[c] + 1
            // 2. There are two Posts in each Column.
            // 3. The numbers on the side tell you the length of the cables between
            // the two Posts (in that Column).
            val s = if (n1 < 2) HintState.Normal else if (n1 == 2 && (n2 == PowerGridGame.PUZ_UNKNOWN + 1 || n2 == posts[1].row - posts[0].row)) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
            if (s == HintState.Error)
                for (p in posts)
                    pos2state[p] = AllowedObjectState.Error
            if (allowedObjectsOnly && n1 > 0)
                for (r in 0..<rows)
                    if (this[r, c] == PowerGridObject.Empty && (n1 > 1 || n2 != PowerGridGame.PUZ_UNKNOWN + 1 && n2 != abs(posts[0].row - r)))
                        this[r, c] = PowerGridObject.Forbidden
        }
    }
}