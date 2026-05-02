package com.zwstudio.logicpuzzlesandroid.puzzles.themagicnumber

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TheMagicNumberGameState(game: TheMagicNumberGame) : CellsGameState<TheMagicNumberGame, TheMagicNumberGameMove, TheMagicNumberGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: TheMagicNumberObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: TheMagicNumberObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: TheMagicNumberGameMove): GameOperationType {
        if (!isValid(move.p) || game[move.p] != TheMagicNumberObject.Empty || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TheMagicNumberGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != TheMagicNumberObject.Empty) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            TheMagicNumberObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) TheMagicNumberObject.Marker else TheMagicNumberObject.Flower
            TheMagicNumberObject.Flower -> if (markerOption == MarkerOptions.MarkerLast) TheMagicNumberObject.Marker else TheMagicNumberObject.Empty
            TheMagicNumberObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) TheMagicNumberObject.Flower else TheMagicNumberObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 3/The Magic Number

        Summary
        No more, no less, you don't have to guess

        Description
        1. Fill the board with 3 different symbols.
        2. On side-6 boards there will be 2 of each on any row or column.
        3. On side-9 boards there will be 3 of each on any row or column.
        4. On side-12 boards there will be 4 of each on any row or column.
        5. When a tile has a shaded background, the symbols around it must
           be different.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (this[p]) {
                    TheMagicNumberObject.Forbidden ->
                        this[p] = TheMagicNumberObject.Empty
                    TheMagicNumberObject.Flower -> {
                        pos2state[p] = AllowedObjectState.Normal
                        val node = Node(p.toString())
                        g.addNode(node)
                        pos2node[p] = node
                    }
                    else -> {}
                }
            }
        for ((p, node) in pos2node)
            for (os in TheMagicNumberGame.offset) {
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
                val os = TheMagicNumberGame.offset[i]
                var p2 = p + os
                while (isValid(p2) && this[p2] == TheMagicNumberObject.Flower) {
                    flowers.add(p2)
                    p2 += os
                }
            }
            if (flowers.size > 2)
                this[p] = TheMagicNumberObject.Forbidden
            flowers.clear()
        }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == TheMagicNumberObject.Flower)
                    flowers.add(p)
                else {
                    checkFlowers()
                    if (o == TheMagicNumberObject.Empty || o == TheMagicNumberObject.Marker)
                        checkForbidden(p, listOf(1, 3))
                }
            }
            checkFlowers()
        }
        for (c in 0 until cols) {
            for (r in 0 until rows) {
                val p = Position(r, c)
                val o = this[p]
                if (o == TheMagicNumberObject.Flower)
                    flowers.add(p)
                else {
                    checkFlowers()
                    if (o == TheMagicNumberObject.Empty || o == TheMagicNumberObject.Marker)
                        checkForbidden(p, listOf(0, 2))
                }
            }
            checkFlowers()
        }
    }
}