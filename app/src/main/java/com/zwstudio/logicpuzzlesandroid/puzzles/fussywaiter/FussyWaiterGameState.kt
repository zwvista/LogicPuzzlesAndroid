package com.zwstudio.logicpuzzlesandroid.puzzles.fussywaiter

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FussyWaiterGameState(game: FussyWaiterGame) : CellsGameState<FussyWaiterGame, FussyWaiterGameMove, FussyWaiterGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: FussyWaiterObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: FussyWaiterObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FussyWaiterGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] !is FussyWaiterEmptyObject || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FussyWaiterGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] !is FussyWaiterEmptyObject) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is FussyWaiterEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) FussyWaiterMarkerObject else FussyWaiterFlowerObject()
            is FussyWaiterFlowerObject -> if (markerOption == MarkerOptions.MarkerLast) FussyWaiterMarkerObject else FussyWaiterEmptyObject
            is FussyWaiterMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) FussyWaiterFlowerObject() else FussyWaiterEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 15/Fussy Waiter

        Summary
        Won't give you what you asked for

        Description
        1. This restaurant has a peculiar waiter. Priding himself on a math
           degree, he is very fussy about how you order.
        2. Respecting university nutrition balance, he only accepts unique
           pairings of food and drinks.
        3. Thus, a type of food can be ordered along with the same drink only
           on a single table.
        4. Moreover, touting sudoku nutrition, he also maintains that each row
           and column of tables must have each food and drinks represented
           exactly once.
        5. He is indeed, very fussy.
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
                if (o is FussyWaiterForbiddenObject)
                    this[p] = FussyWaiterEmptyObject
                else if (o is FussyWaiterFlowerObject) {
                    o.state = AllowedObjectState.Normal
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (os in FussyWaiterGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
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
                    (this[p] as FussyWaiterFlowerObject).state = AllowedObjectState.Error
            }
            flowers.clear()
        }
        fun checkForbidden(p: Position, indexes: List<Int>) {
            if (!allowedObjectsOnly) return
            for (i in indexes) {
                val os = FussyWaiterGame.offset[i]
                var p2 = p + os
                while (isValid(p2) && this[p2] is FussyWaiterFlowerObject) {
                    flowers.add(p2)
                    p2 += os
                }
            }
            if (areFlowersInvalid()) this[p] = FussyWaiterForbiddenObject
            flowers.clear()
        }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is FussyWaiterFlowerObject)
                    flowers.add(p)
                else {
                    checkFlowers()
                    if (o is FussyWaiterEmptyObject || o is FussyWaiterMarkerObject)
                        checkForbidden(p, listOf(1, 3))
                }
            }
            checkFlowers()
        }
        for (c in 0 until cols) {
            for (r in 0 until rows) {
                val p = Position(r, c)
                val o = get(p)
                if (o is FussyWaiterFlowerObject)
                    flowers.add(p)
                else {
                    checkFlowers()
                    if (o is FussyWaiterEmptyObject || o is FussyWaiterMarkerObject)
                        checkForbidden(p, listOf(0, 2))
                }
            }
            checkFlowers()
        }
    }
}