package com.zwstudio.logicpuzzlesandroid.puzzles.tents.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TentsGameState(game: TentsGame) : CellsGameState<TentsGame, TentsGameMove, TentsGameState>(game) {
    var objArray: Array<TentsObject?>
    var row2state: Array<HintState?>
    var col2state: Array<HintState?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: TentsObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: TentsObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: TentsGameMove): Boolean {
        if (!isValid(move.p) || get(move.p) === move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: TentsGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<TentsObject, TentsObject> = label@ F<TentsObject, TentsObject> { obj: TentsObject? ->
            if (obj is TentsEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) TentsMarkerObject() else TentsTentObject()
            if (obj is TentsTentObject) return@label if (markerOption == MarkerOptions.MarkerLast) TentsMarkerObject() else TentsEmptyObject()
            if (obj is TentsMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) TentsTentObject() else TentsEmptyObject()
            obj
        }
        val p: Position = move.p
        if (!isValid(p)) return false
        move.obj = f.f(get(p))
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/Tents

        Summary
        Each camper wants to put his Tent under the shade of a Tree. But he also
        wants his privacy!

        Description
        1. The board represents a camping field with many Trees. Campers want to set
           their Tent in the shade, horizontally or vertically adjacent to a Tree(not
           diagonally).
        2. At the same time they need their privacy, so a Tent can't have any other
           Tents near them, not even diagonally.
        3. The numbers on the borders tell you how many Tents there are in that row
           or column.
        4. Finally, each Tree has at least one Tent touching it, horizontally or
           vertically.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        for (r in 0 until rows()) {
            var n1 = 0
            val n2: Int = game.row2hint.get(r)
            for (c in 0 until cols()) if (get(r, c) is TentsTentObject) n1++
            // 3. The numbers on the borders tell you how many Tents there are in that row.
            row2state[r] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (c in 0 until cols()) {
            var n1 = 0
            val n2: Int = game.col2hint.get(c)
            for (r in 0 until rows()) if (get(r, c) is TentsTentObject) n1++
            // 3. The numbers on the borders tell you how many Tents there are in that column.
            col2state[c] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val o: TentsObject? = get(r, c)
            if (o is TentsForbiddenObject) set(r, c, TentsEmptyObject())
        }
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o: TentsObject? = get(r, c)
            val hasTree: F0<Boolean> = label@ F0<Boolean> {
                for (os in TentsGame.offset) {
                    val p2 = p.add(os)
                    if (isValid(p2) && get(p2) is TentsTreeObject) return@label true
                }
                false
            }
            val hasTent: F<Boolean, Boolean> = label@ F<Boolean, Boolean> { isTree: Boolean ->
                for (os in if (isTree) TentsGame.offset else TentsGame.offset2) {
                    val p2 = p.add(os)
                    if (isValid(p2) && get(p2) is TentsTentObject) return@label true
                }
                false
            }
            if (o is TentsTentObject) {
                // 1. The board represents a camping field with many Trees. Campers want to set
                // their Tent in the shade, horizontally or vertically adjacent to a Tree(not
                // diagonally).
                // 2. At the same time they need their privacy, so a Tent can't have any other
                // Tents near them, not even diagonally.
                val s: AllowedObjectState = if (hasTree.f() && !hasTent.f(false)) AllowedObjectState.Normal else AllowedObjectState.Error
                o.state = s
                if (s == AllowedObjectState.Error) isSolved = false
            } else if (o is TentsTreeObject) {
                // 4. Finally, each Tree has at least one Tent touching it, horizontally or
                // vertically.
                val s: AllowedObjectState = if (hasTent.f(true)) AllowedObjectState.Normal else AllowedObjectState.Error
                o.state = s
                if (s == AllowedObjectState.Error) isSolved = false
            } else if ((o is TentsEmptyObject || o is TentsMarkerObject) && allowedObjectsOnly &&
                (col2state[c] != HintState.Normal || row2state[r] != HintState.Normal || !hasTree.f() || hasTent.f(false))) set(r, c, TentsForbiddenObject())
        }
    }

    init {
        objArray = arrayOfNulls<TentsObject>(rows() * cols())
        for (i in objArray.indices) objArray[i] = TentsEmptyObject()
        for (p in game.pos2tree) set(p, TentsTreeObject())
        row2state = arrayOfNulls<HintState>(rows())
        col2state = arrayOfNulls<HintState>(cols())
        updateIsSolved()
    }
}