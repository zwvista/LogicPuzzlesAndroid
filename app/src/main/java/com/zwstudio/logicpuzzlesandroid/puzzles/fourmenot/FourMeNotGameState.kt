package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FourMeNotGameState(game: FourMeNotGame) : CellsGameState<FourMeNotGame, FourMeNotGameMove, FourMeNotGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: FourMeNotObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: FourMeNotObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FourMeNotGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != FourMeNotObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FourMeNotGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != FourMeNotObject.Empty) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            FourMeNotObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) FourMeNotObject.Marker else FourMeNotObject.Flower
            FourMeNotObject.Flower -> if (markerOption == MarkerOptions.MarkerLast) FourMeNotObject.Marker else FourMeNotObject.Empty
            FourMeNotObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) FourMeNotObject.Flower else FourMeNotObject.Empty
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
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                when (this[p]) {
                    FourMeNotObject.Forbidden ->
                        this[p] = FourMeNotObject.Empty
                    FourMeNotObject.Flower -> {
                        pos2state[p] = AllowedObjectState.Normal
                        val node = Node(p.toString())
                        g.addNode(node)
                        pos2node[p] = node
                    }
                    else -> {}
                }
            }
        for ((p, node) in pos2node)
            for (os in FourMeNotGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
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
        fun checkFlowers() {
            if (flowers.size > 3) {
                isSolved = false
                for (p in flowers)
                    pos2state[p] = AllowedObjectState.Error
            }
            flowers.clear()
        }
        fun checkForbidden(p: Position, indexes: List<Int>) {
            if (!allowedObjectsOnly) return
            for (i in indexes) {
                val os = FourMeNotGame.offset[i]
                var p2 = p + os
                while (isValid(p2) && this[p2] == FourMeNotObject.Flower) {
                    flowers.add(p2)
                    p2 += os
                }
            }
            if (flowers.size > 2)
                this[p] = FourMeNotObject.Forbidden
            flowers.clear()
        }
        for (r in 0..<rows) {
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == FourMeNotObject.Flower)
                    flowers.add(p)
                else {
                    checkFlowers()
                    if (o == FourMeNotObject.Empty || o == FourMeNotObject.Marker)
                        checkForbidden(p, listOf(1, 3))
                }
            }
            checkFlowers()
        }
        for (c in 0..<cols) {
            for (r in 0..<rows) {
                val p = Position(r, c)
                val o = this[p]
                if (o == FourMeNotObject.Flower)
                    flowers.add(p)
                else {
                    checkFlowers()
                    if (o == FourMeNotObject.Empty || o == FourMeNotObject.Marker)
                        checkForbidden(p, listOf(0, 2))
                }
            }
            checkFlowers()
        }
    }
}