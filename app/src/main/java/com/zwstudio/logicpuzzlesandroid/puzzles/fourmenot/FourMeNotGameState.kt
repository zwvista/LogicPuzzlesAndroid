package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameChangeType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FourMeNotGameState(game: FourMeNotGame) : CellsGameState<FourMeNotGame, FourMeNotGameMove, FourMeNotGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: FourMeNotObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: FourMeNotObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FourMeNotGameMove): GameChangeType {
        if (!isValid(move.p) || game[move.p] !is FourMeNotEmptyObject || this[move.p] == move.obj) return GameChangeType.None
        this[move.p] = move.obj
        updateIsSolved()
        return GameChangeType.Level
    }

    override fun switchObject(move: FourMeNotGameMove): GameChangeType {
        if (!isValid(move.p) || game[move.p] !is FourMeNotEmptyObject) return GameChangeType.None
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is FourMeNotEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) FourMeNotMarkerObject else FourMeNotFlowerObject()
            is FourMeNotFlowerObject -> if (markerOption == MarkerOptions.MarkerLast) FourMeNotMarkerObject else FourMeNotEmptyObject
            is FourMeNotMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) FourMeNotFlowerObject() else FourMeNotEmptyObject
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
                    this[p] = FourMeNotEmptyObject
                else if (o is FourMeNotFlowerObject) {
                    o.state = AllowedObjectState.Normal
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (os in FourMeNotGame.offset) {
                val p2 = p + os
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
        val flowers = mutableListOf<Position>()
        // 3. At the same time, you can't line up horizontally or vertically more
        // than 3 flowers (thus Forbidden Four).
        fun areFlowersInvalid() = flowers.size > 3
        fun checkFlowers() {
            if (areFlowersInvalid()) {
                isSolved = false
                for (p in flowers)
                    (this[p] as FourMeNotFlowerObject).state = AllowedObjectState.Error
            }
            flowers.clear()
        }
        fun checkForbidden(p: Position, indexes: List<Int>) {
            if (!allowedObjectsOnly) return
            for (i in indexes) {
                val os = FourMeNotGame.offset[i]
                var p2 = p + os
                while (isValid(p2) && this[p2] is FourMeNotFlowerObject) {
                    flowers.add(p2)
                    p2 += os
                }
            }
            if (areFlowersInvalid()) this[p] = FourMeNotForbiddenObject
            flowers.clear()
        }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is FourMeNotFlowerObject)
                    flowers.add(p)
                else {
                    checkFlowers()
                    if (o is FourMeNotEmptyObject || o is FourMeNotMarkerObject)
                        checkForbidden(p, listOf(1, 3))
                }
            }
            checkFlowers()
        }
        for (c in 0 until cols) {
            for (r in 0 until rows) {
                val p = Position(r, c)
                val o = get(p)
                if (o is FourMeNotFlowerObject)
                    flowers.add(p)
                else {
                    checkFlowers()
                    if (o is FourMeNotEmptyObject || o is FourMeNotMarkerObject)
                        checkForbidden(p, listOf(0, 2))
                }
            }
            checkFlowers()
        }
    }
}