package com.zwstudio.logicpuzzlesandroid.puzzles.parks

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ParksGameState(game: ParksGame) : CellsGameState<ParksGame, ParksGameMove, ParksGameState>(game) {
    var objArray = Array(rows * cols) { ParksObject.Empty }
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: ParksObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: ParksObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: ParksGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ParksGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            ParksObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) ParksObject.Marker else ParksObject.Tree
            ParksObject.Tree -> if (markerOption == MarkerOptions.MarkerLast) ParksObject.Marker else ParksObject.Empty
            ParksObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) ParksObject.Tree else ParksObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/Parks

        Summary
        Put one Tree in each Park, row and column.(two in bigger levels)

        Description
        1. In Parks, you have many differently coloured areas(Parks) on the board.
        2. The goal is to plant Trees, following these rules:
        3. A Tree can't touch another Tree, not even diagonally.
        4. Each park must have exactly ONE Tree.
        5. There must be exactly ONE Tree in each row and each column.
        6. Remember a Tree CANNOT touch another Tree diagonally,
           but it CAN be on the same diagonal line.
        7. Larger puzzles have TWO Trees in each park, each row and each column.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
            if (this[r, c] == ParksObject.Forbidden)
                this[r, c] = ParksObject.Empty
        // 3. A Tree can't touch another Tree, not even diagonally.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                fun hasNeighbor() = ParksGame.offset.any {
                    val p2 = p + it
                    isValid(p2) && this[p2] == ParksObject.Tree
                }
                val o = this[p]
                if (o == ParksObject.Tree)
                    pos2state[p] = if (!hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o == ParksObject.Empty || o == ParksObject.Marker) && allowedObjectsOnly && hasNeighbor())
                    this[p] = ParksObject.Forbidden
            }
        val n2 = game.treesInEachArea
        // 5. There must be exactly ONE Tree in each row.
        for (r in 0 until rows) {
            var n1 = 0
            for (c in 0 until cols)
                if (this[r, c] == ParksObject.Tree)
                    n1++
            if (n1 != n2) isSolved = false
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == ParksObject.Tree)
                    pos2state[p] = if (pos2state[p] == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o == ParksObject.Empty || o == ParksObject.Marker) && n1 >= n2 && allowedObjectsOnly)
                    this[p] = ParksObject.Forbidden
            }
        }
        // 5. There must be exactly ONE Tree in each column.
        for (c in 0 until cols) {
            var n1 = 0
            for (r in 0 until rows)
                if (this[r, c] == ParksObject.Tree)
                    n1++
            if (n1 != n2) isSolved = false
            for (r in 0 until rows) {
                val p = Position(r, c)
                val o = this[p]
                if (o == ParksObject.Tree)
                    pos2state[p] = if (pos2state[p] == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o == ParksObject.Empty || o == ParksObject.Marker) && n1 >= n2 && allowedObjectsOnly)
                    this[p] = ParksObject.Forbidden
            }
        }
        // 4. Each park must have exactly ONE Tree.
        for (a in game.areas) {
            var n1 = 0
            for (p in a)
                if (this[p] == ParksObject.Tree)
                    n1++
            if (n1 != n2) isSolved = false
            for (p in a) {
                val o = this[p]
                if (o == ParksObject.Tree)
                    pos2state[p] = if (pos2state[p] == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o == ParksObject.Empty || o == ParksObject.Marker) && n1 >= n2 && allowedObjectsOnly)
                    this[p] = ParksObject.Forbidden
            }
        }
    }
}