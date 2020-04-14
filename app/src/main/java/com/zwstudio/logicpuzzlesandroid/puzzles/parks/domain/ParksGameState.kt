package com.zwstudio.logicpuzzlesandroid.puzzles.parks.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import java.util.*

class ParksGameState(game: ParksGame?) : CellsGameState<ParksGame?, ParksGameMove?, ParksGameState?>(game) {
    var objArray: Array<ParksObject?>
    var pos2state: Map<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: ParksObject?) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: ParksObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: ParksGameMove): Boolean {
        if (!isValid(move.p) || get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: ParksGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<ParksObject, ParksObject> = label@ F<ParksObject, ParksObject> { obj: ParksObject? ->
            if (obj is ParksEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) ParksMarkerObject() else ParksTreeObject()
            if (obj is ParksTreeObject) return@label if (markerOption == MarkerOptions.MarkerLast) ParksMarkerObject() else ParksEmptyObject()
            if (obj is ParksMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) ParksTreeObject() else ParksEmptyObject()
            obj
        }
        val o: ParksObject? = get(move.p)
        move.obj = f.f(o)
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
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val o: ParksObject? = get(r, c)
            if (o is ParksForbiddenObject) set(r, c, ParksEmptyObject())
        }
        // 3. A Tree can't touch another Tree, not even diagonally.
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val hasNeighbor: F0<Boolean> = F0<Boolean> {
                fj.data.Array.array<Position>(*ParksGame.Companion.offset).exists(F<Position, Boolean> { os: Position? ->
                    val p2 = p.add(os)
                    isValid(p2) && get(p2) is ParksTreeObject
                })
            }
            val o: ParksObject? = get(r, c)
            if (o is ParksTreeObject) {
                o.state = if (!hasNeighbor.f()) AllowedObjectState.Normal else AllowedObjectState.Error
            } else if ((o is ParksEmptyObject || o is ParksMarkerObject) && allowedObjectsOnly && hasNeighbor.f()) set(r, c, ParksForbiddenObject())
        }
        val n2: Int = game.treesInEachArea
        // 5. There must be exactly ONE Tree in each row.
        for (r in 0 until rows()) {
            var n1 = 0
            for (c in 0 until cols()) if (get(r, c) is ParksTreeObject) n1++
            if (n1 != n2) isSolved = false
            for (c in 0 until cols()) {
                val o: ParksObject? = get(r, c)
                if (o is ParksTreeObject) {
                    val o2: ParksTreeObject = o
                    o2.state = if (o2.state == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                } else if ((o is ParksEmptyObject || o is ParksMarkerObject) && n1 >= n2 && allowedObjectsOnly) set(r, c, ParksForbiddenObject())
            }
        }
        // 5. There must be exactly ONE Tree in each column.
        for (c in 0 until cols()) {
            var n1 = 0
            for (r in 0 until rows()) if (get(r, c) is ParksTreeObject) n1++
            if (n1 != n2) isSolved = false
            for (r in 0 until rows()) {
                val o: ParksObject? = get(r, c)
                if (o is ParksTreeObject) {
                    val o2: ParksTreeObject = o
                    o2.state = if (o2.state == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                } else if ((o is ParksEmptyObject || o is ParksMarkerObject) && n1 >= n2 && allowedObjectsOnly) set(r, c, ParksForbiddenObject())
            }
        }
        // 4. Each park must have exactly ONE Tree.
        for (a in game.areas) {
            var n1 = 0
            for (p in a) if (get(p) is ParksTreeObject) n1++
            if (n1 != n2) isSolved = false
            for (p in a) {
                val o: ParksObject? = get(p)
                if (o is ParksTreeObject) {
                    val o2: ParksTreeObject = o
                    o2.state = if (o2.state == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                } else if ((o is ParksEmptyObject || o is ParksMarkerObject) && n1 >= n2 && allowedObjectsOnly) set(p, ParksForbiddenObject())
            }
        }
    }

    init {
        objArray = arrayOfNulls<ParksObject>(rows() * cols())
        for (i in objArray.indices) objArray[i] = ParksEmptyObject()
    }
}