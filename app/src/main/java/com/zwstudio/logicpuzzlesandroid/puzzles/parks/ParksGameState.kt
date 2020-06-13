package com.zwstudio.logicpuzzlesandroid.puzzles.parks

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class ParksGameState(game: ParksGame) : CellsGameState<ParksGame, ParksGameMove, ParksGameState>(game) {
    var objArray = Array<ParksObject>(rows * cols) { ParksEmptyObject() }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: ParksObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: ParksObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: ParksGameMove): Boolean {
        if (!isValid(move.p) || this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: ParksGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is ParksEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) ParksMarkerObject() else ParksTreeObject()
            is ParksTreeObject -> if (markerOption == MarkerOptions.MarkerLast) ParksMarkerObject() else ParksEmptyObject()
            is ParksMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) ParksTreeObject() else ParksEmptyObject()
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
            if (this[r, c] is ParksForbiddenObject)
                this[r, c] = ParksEmptyObject()
        // 3. A Tree can't touch another Tree, not even diagonally.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                fun hasNeighbor() = ParksGame.offset.any {
                    val p2 = p + it
                    isValid(p2) && this[p2] is ParksTreeObject
                }
                val o = this[r, c]
                if (o is ParksTreeObject)
                    o.state = if (!hasNeighbor()) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is ParksEmptyObject || o is ParksMarkerObject) && allowedObjectsOnly && hasNeighbor())
                    this[r, c] = ParksForbiddenObject()
            }
        val n2: Int = game.treesInEachArea
        // 5. There must be exactly ONE Tree in each row.
        for (r in 0 until rows) {
            var n1 = 0
            for (c in 0 until cols)
                if (this[r, c] is ParksTreeObject)
                    n1++
            if (n1 != n2) isSolved = false
            for (c in 0 until cols) {
                val o = this[r, c]
                if (o is ParksTreeObject)
                    o.state = if (o.state == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is ParksEmptyObject || o is ParksMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    this[r, c] = ParksForbiddenObject()
            }
        }
        // 5. There must be exactly ONE Tree in each column.
        for (c in 0 until cols) {
            var n1 = 0
            for (r in 0 until rows)
                if (this[r, c] is ParksTreeObject)
                    n1++
            if (n1 != n2) isSolved = false
            for (r in 0 until rows) {
                val o = this[r, c]
                if (o is ParksTreeObject)
                    o.state = if (o.state == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is ParksEmptyObject || o is ParksMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    this[r, c] = ParksForbiddenObject()
            }
        }
        // 4. Each park must have exactly ONE Tree.
        for (a in game.areas) {
            var n1 = 0
            for (p in a)
                if (this[p] is ParksTreeObject)
                    n1++
            if (n1 != n2) isSolved = false
            for (p in a) {
                val o = this[p]
                if (o is ParksTreeObject)
                    o.state = if (o.state == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if ((o is ParksEmptyObject || o is ParksMarkerObject) && n1 >= n2 && allowedObjectsOnly)
                    this[p] = ParksForbiddenObject()
            }
        }
    }
}