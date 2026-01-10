package com.zwstudio.logicpuzzlesandroid.puzzles.trebuchet

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.GardenerGame

class TrebuchetGameState(game: TrebuchetGame) : CellsGameState<TrebuchetGame, TrebuchetGameMove, TrebuchetGameState>(game) {
    var objArray = Array<TrebuchetObject>(rows * cols) { TrebuchetEmptyObject }
    val invalid2x2Squares = mutableListOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: TrebuchetObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: TrebuchetObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = TrebuchetHintObject()
        updateIsSolved()
    }

    override fun setObject(move: TrebuchetGameMove): GameOperationType {
        if (!isValid(move.p) || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TrebuchetGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[move.p]) {
            is TrebuchetEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) TrebuchetMarkerObject else TrebuchetDuneObject()
            is TrebuchetDuneObject -> if (markerOption == MarkerOptions.MarkerLast) TrebuchetMarkerObject else TrebuchetEmptyObject
            is TrebuchetMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) TrebuchetDuneObject() else TrebuchetEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 6/Trebuchet

        Summary
        Fire!

        Description
        1. On the board you can see some trebuchets.
        2. The number on a Trebuchet indicates the distance it shoots. Only one of
           the four directions can be marked with a target, the others should be empty.
        3. Two target cells must not be orthogonally adjacent.
        4. All of the non-targeted cells must be connected.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is TrebuchetForbiddenObject)
                    this[r, c] = TrebuchetEmptyObject
        // 5. No area of desert of 2x2 should be empty of Dunes.
        invalid2x2Squares.clear()
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val isEmptyOfDunes = TrebuchetGame.offset2.map { p + it }.all { this[it] !is TrebuchetDuneObject }
                if (isEmptyOfDunes) { invalid2x2Squares.add(p + Position.SouthEast); isSolved = false }
            }
        // 4. Dunes cannot touch each other horizontally or vertically.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] !is TrebuchetDuneObject) continue
                for (os in TrebuchetGame.offset) {
                    val p2 = p + os
                    if (!isValid(p2)) continue
                    if (this[p2] is TrebuchetDuneObject) {
                        isSolved = false
                        this[p] = TrebuchetDuneObject(AllowedObjectState.Error)
                        this[p2] = TrebuchetDuneObject(AllowedObjectState.Error)
                    } else if (allowedObjectsOnly && this[p2] is TrebuchetEmptyObject)
                        this[p2] = TrebuchetForbiddenObject
                }
            }
        // 2. The desert among dunes (including oases) should be all connected
        //    horizontally or vertically.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] is TrebuchetDuneObject) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node) {
            for (os in GardenerGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        }
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
        for ((p, _) in game.pos2hint) {
            val index = g.nodes.indexOf(pos2node[p]!!)
            g.adjMatrix!![index] = IntArray(g.size) { 0 }
        }
        // 1. Put some dunes on the desert so that each Oasis dweller can reach the
        //    number of Oases marked on it.
        for ((p, n2) in game.pos2hint) {
            val hints = mutableSetOf<Position>()
            // 3. Dwellers can move horizontally or vertically.
            TrebuchetGame.offset.map { p + it }
                .filter { isValid(it) && this[it] !is TrebuchetDuneObject }
                .forEach { g.connectNode(pos2node[p]!!, pos2node[it]!!) }
            g.rootNode = pos2node[p]!!
            val nodeList = g.bfs()
            val index = g.nodes.indexOf(pos2node[p]!!)
            g.adjMatrix!![index] = IntArray(g.size) { 0 }
            nodeList
                .map { node -> pos2node.firstNotNullOf { (k, v) -> if (v == node) k else null } }
                .filter { this[it] is TrebuchetHintObject }
                .forEach { hints.add(it) }
            hints.remove(p)
            val n1 = hints.size
            val s = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            this[p] = TrebuchetHintObject(s)
            if (s != HintState.Complete) isSolved = false
        }
    }
}