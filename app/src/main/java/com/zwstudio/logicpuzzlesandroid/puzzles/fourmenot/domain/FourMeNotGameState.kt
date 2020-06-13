package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import java.util.*

class FourMeNotGameState(game: FourMeNotGame) : CellsGameState<FourMeNotGame, FourMeNotGameMove, FourMeNotGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state: Map<Position, HintState> = HashMap()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: FourMeNotObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: FourMeNotObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: FourMeNotGameMove): Boolean {
        if (!isValid(move.p) || game[move.p] !is FourMeNotEmptyObject || this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: FourMeNotGameMove): Boolean {
        if (!isValid(move.p) || game[move.p] !is FourMeNotEmptyObject) return false
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is FourMeNotEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) FourMeNotMarkerObject() else FourMeNotTreeObject()
            is FourMeNotTreeObject -> if (markerOption == MarkerOptions.MarkerLast) FourMeNotMarkerObject() else FourMeNotEmptyObject()
            is FourMeNotMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) FourMeNotTreeObject() else FourMeNotEmptyObject()
            else -> o
        }
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
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is FourMeNotForbiddenObject)
                    this[p] = FourMeNotEmptyObject()
                else if (o is FourMeNotTreeObject) {
                    o.state = AllowedObjectState.Normal
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (os in FourMeNotGame.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
            }
        }
        // 2. More exactly, you have to join the existing flowers by adding more of
        // them, creating a single path of flowers touching horizontally or
        // vertically.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
        val trees = mutableListOf<Position>()
        // 3. At the same time, you can't line up horizontally or vertically more
        // than 3 flowers (thus Forbidden Four).
        fun areTreesInvalid() = trees.size > 3
        fun checkTrees() {
            if (areTreesInvalid()) {
                isSolved = false
                for (p in trees)
                    (this[p] as FourMeNotTreeObject).state = AllowedObjectState.Error
            }
            trees.clear()
        }
        fun checkForbidden(p: Position, indexes: List<Int>) {
            if (!allowedObjectsOnly) return
            for (i in indexes) {
                val os: Position = FourMeNotGame.offset[i]
                val p2 = p.add(os)
                while (isValid(p2) && this[p2] is FourMeNotTreeObject) {
                    trees.add(p2)
                    p2.addBy(os)
                }
            }
            if (areTreesInvalid()) this[p] = FourMeNotForbiddenObject()
            trees.clear()
        }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is FourMeNotTreeObject)
                    trees.add(p)
                else {
                    checkTrees()
                    if (o is FourMeNotEmptyObject || o is FourMeNotMarkerObject)
                        checkForbidden(p, listOf(1, 3))
                }
            }
            checkTrees()
        }
        for (c in 0 until cols) {
            for (r in 0 until rows) {
                val p = Position(r, c)
                val o = get(p)
                if (o is FourMeNotTreeObject)
                    trees.add(p)
                else {
                    checkTrees()
                    if (o is FourMeNotEmptyObject || o is FourMeNotMarkerObject)
                        checkForbidden(p, listOf(0, 2))
                }
            }
            checkTrees()
        }
    }
}