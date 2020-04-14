package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.F0
import fj.function.Effect0
import fj.function.Effect2
import java.util.*

class FourMeNotGameState(game: FourMeNotGame) : CellsGameState<FourMeNotGame?, FourMeNotGameMove?, FourMeNotGameState?>(game) {
    var objArray: Array<FourMeNotObject?>
    var pos2state: Map<Position?, HintState?> = HashMap()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position?) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: FourMeNotObject?) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position?, obj: FourMeNotObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: FourMeNotGameMove?): Boolean {
        if (!isValid(move!!.p) || game!![move.p] !is FourMeNotEmptyObject || get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: FourMeNotGameMove?): Boolean {
        if (!isValid(move!!.p) || game!![move.p] !is FourMeNotEmptyObject) return false
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: FourMeNotObject? ->
            if (obj is FourMeNotEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) FourMeNotMarkerObject() else FourMeNotTreeObject()
            if (obj is FourMeNotTreeObject) return@label if (markerOption == MarkerOptions.MarkerLast) FourMeNotMarkerObject() else FourMeNotEmptyObject()
            if (obj is FourMeNotMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) FourMeNotTreeObject() else FourMeNotEmptyObject()
            obj
        }
        val o = get(move.p)
        move.obj = f.f(o)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/Four-Me-Not

        Summary
        It seems we do a lot of gardening in this game!

        Description
        1. In Four-Me-Not (or Forbidden Four) you need to create a continuous
           flower bed without putting four flowers in a row.
        2. More exactly, you have to join the existing flowers by adding more of
           them, creating a single path of flowers touching horizontally or
           vertically.
        3. At the same time, you can't line up horizontally or vertically more
           than 3 flowers (thus Forbidden Four).
        4. Some tiles are marked as squares and are just fixed blocks.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game!!.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node: MutableMap<Position, Node> = HashMap()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o = get(p)
            if (o is FourMeNotForbiddenObject) set(p, FourMeNotEmptyObject()) else if (o is FourMeNotTreeObject) {
                o.state = AllowedObjectState.Normal
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for ((p, node) in pos2node) {
            for (os in FourMeNotGame.Companion.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null) g.connectNode(node, node2)
            }
        }
        // 2. More exactly, you have to join the existing flowers by adding more of
        // them, creating a single path of flowers touching horizontally or
        // vertically.
        g.setRootNode(fj.data.HashMap.fromMap(pos2node).values().head())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
        val trees: MutableList<Position> = ArrayList()
        // 3. At the same time, you can't line up horizontally or vertically more
        // than 3 flowers (thus Forbidden Four).
        val areTreesInvalid = F0 { trees.size > 3 }
        val checkTrees = Effect0 {
            if (areTreesInvalid.f()) {
                isSolved = false
                for (p in trees) (get(p) as FourMeNotTreeObject?)!!.state = AllowedObjectState.Error
            }
            trees.clear()
        }
        val checkForbidden = label@ Effect2 { p: Position, indexes: List<Int> ->
            if (!allowedObjectsOnly) return@label
            for (i in indexes) {
                val os: Position = FourMeNotGame.Companion.offset.get(i)
                val p2 = p.add(os)
                while (isValid(p2) && get(p2) is FourMeNotTreeObject) {
                    trees.add(p2)
                    p2.addBy(os)
                }
            }
            if (areTreesInvalid.f()) set(p, FourMeNotForbiddenObject())
            trees.clear()
        }
        for (r in 0 until rows()) {
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val o = get(p)
                if (o is FourMeNotTreeObject) trees.add(p) else {
                    checkTrees.f()
                    if (o is FourMeNotEmptyObject || o is FourMeNotMarkerObject) checkForbidden.f(p, fj.data.List.arrayList(1, 3).toJavaList())
                }
            }
            checkTrees.f()
        }
        for (c in 0 until cols()) {
            for (r in 0 until rows()) {
                val p = Position(r, c)
                val o = get(p)
                if (o is FourMeNotTreeObject) trees.add(p) else {
                    checkTrees.f()
                    if (o is FourMeNotEmptyObject || o is FourMeNotMarkerObject) checkForbidden.f(p, fj.data.List.arrayList(0, 2).toJavaList())
                }
            }
            checkTrees.f()
        }
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.size)
        updateIsSolved()
    }
}