package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import java.util.*

class PowerGridGameState(game: PowerGridGame?) : CellsGameState<PowerGridGame?, PowerGridGameMove?, PowerGridGameState?>(game) {
    var objArray: Array<PowerGridObject?>
    var row2state: Array<HintState?>
    var col2state: Array<HintState?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position?) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: PowerGridObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position?, obj: PowerGridObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: PowerGridGameMove): Boolean {
        if (get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: PowerGridGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<PowerGridObject, PowerGridObject> = label@ F<PowerGridObject, PowerGridObject> { obj: PowerGridObject? ->
            if (obj is PowerGridEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) PowerGridMarkerObject() else PowerGridPostObject()
            if (obj is PowerGridPostObject) return@label if (markerOption == MarkerOptions.MarkerLast) PowerGridMarkerObject() else PowerGridEmptyObject()
            if (obj is PowerGridMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) PowerGridPostObject() else PowerGridEmptyObject()
            obj
        }
        val o: PowerGridObject? = get(move.p)
        move.obj = f.f(o)
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
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val o: PowerGridObject? = get(r, c)
            if (o is PowerGridForbiddenObject) set(r, c, PowerGridEmptyObject()) else if (o is PowerGridPostObject) o.state = AllowedObjectState.Normal
        }
        for (r in 0 until rows()) {
            val posts = mutableListOf<Position>()
            for (c in 0 until cols()) {
                val p = Position(r, c)
                if (get(p) is PowerGridPostObject) posts.add(p)
            }
            val n1 = posts.size
            val n2: Int = game.row2hint.get(r) + 1
            // 2. There are two Posts in each Row.
            // 3. The numbers on the side tell you the length of the cables between
            // the two Posts (in that Row).
            val s: HintState = if (n1 < 2) HintState.Normal else if (n1 == 2 && n2 == posts[1].col - posts[0].col) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
            if (s == HintState.Error) for (p in posts) (get(p) as PowerGridPostObject?)!!.state = AllowedObjectState.Error
            if (allowedObjectsOnly && n1 > 0) for (c in 0 until cols()) if (get(r, c) is PowerGridEmptyObject && (n1 > 1 || n1 == 1 && n2 != Math.abs(posts[0].col - c))) set(r, c, PowerGridForbiddenObject())
        }
        for (c in 0 until cols()) {
            val posts = mutableListOf<Position>()
            for (r in 0 until rows()) {
                val p = Position(r, c)
                if (get(p) is PowerGridPostObject) posts.add(p)
            }
            val n1 = posts.size
            val n2: Int = game.col2hint.get(c) + 1
            // 2. There are two Posts in each Column.
            // 3. The numbers on the side tell you the length of the cables between
            // the two Posts (in that Column).
            val s: HintState = if (n1 < 2) HintState.Normal else if (n1 == 2 && n2 == posts[1].row - posts[0].row) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
            if (s == HintState.Error) for (p in posts) (get(p) as PowerGridPostObject?)!!.state = AllowedObjectState.Error
            if (allowedObjectsOnly && n1 > 0) for (r in 0 until rows()) if (get(r, c) is PowerGridEmptyObject && (n1 > 1 || n1 == 1 && n2 != Math.abs(posts[0].row - r))) set(r, c, PowerGridForbiddenObject())
        }
    }

    init {
        objArray = arrayOfNulls<PowerGridObject>(rows() * cols())
        Arrays.fill(objArray, PowerGridEmptyObject())
        row2state = arrayOfNulls<HintState>(rows())
        col2state = arrayOfNulls<HintState>(cols())
        updateIsSolved()
    }
}