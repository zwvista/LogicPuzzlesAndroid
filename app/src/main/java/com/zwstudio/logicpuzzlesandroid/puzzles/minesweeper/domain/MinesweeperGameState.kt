package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import java.util.*

class MinesweeperGameState(game: MinesweeperGame) : CellsGameState<MinesweeperGame, MinesweeperGameMove, MinesweeperGameState>(game) {
    var objArray: Array<MinesweeperObject?>
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: MinesweeperObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: MinesweeperObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: MinesweeperGameMove): Boolean {
        if (get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: MinesweeperGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<MinesweeperObject, MinesweeperObject> = label@ F<MinesweeperObject, MinesweeperObject> { obj: MinesweeperObject? ->
            if (obj is MinesweeperEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) MinesweeperMarkerObject() else MinesweeperMineObject() else if (obj is MinesweeperMineObject) return@label if (markerOption == MarkerOptions.MarkerLast) MinesweeperMarkerObject() else MinesweeperEmptyObject() else if (obj is MinesweeperMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) MinesweeperMineObject() else MinesweeperEmptyObject()
            obj
        }
        val o: MinesweeperObject? = get(move.p)
        move.obj = f.f(o)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 6/Minesweeper

        Summary
        You know the drill :)

        Description
        1. Find the mines on the field.
        2. Numbers tell you how many mines there are close by, touching that
           number horizontally, vertically or diagonally.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        for (r in 0 until rows()) for (c in 0 until cols()) if (get(r, c) is MinesweeperForbiddenObject) set(r, c, MinesweeperEmptyObject())
        for ((p, n2) in game.pos2hint.entries) {
            var n1 = 0
            val rng = mutableListOf<Position>()
            for (os in MinesweeperGame.Companion.offset) {
                val p2 = p.add(os)
                if (!isValid(p2)) continue
                val o: MinesweeperObject? = get(p2)
                if (o is MinesweeperMineObject) n1++ else if (o is MinesweeperEmptyObject) rng.add(p2.plus())
            }
            // 2. Numbers tell you how many mines there are close by, touching that
            // number horizontally, vertically or diagonally.
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false else if (allowedObjectsOnly) for (p2 in rng) set(p2, MinesweeperForbiddenObject())
        }
    }

    init {
        objArray = arrayOfNulls<MinesweeperObject>(rows() * cols())
        for (i in objArray.indices) objArray[i] = MinesweeperEmptyObject()
        for (p in game.pos2hint.keys) set(p, MinesweeperHintObject())
        updateIsSolved()
    }
}