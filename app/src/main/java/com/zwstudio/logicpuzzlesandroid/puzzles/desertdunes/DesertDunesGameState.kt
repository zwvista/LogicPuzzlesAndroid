package com.zwstudio.logicpuzzlesandroid.puzzles.desertdunes

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.gardener.GardenerGame

class DesertDunesGameState(game: DesertDunesGame) : CellsGameState<DesertDunesGame, DesertDunesGameMove, DesertDunesGameState>(game) {
    var objArray = Array<DesertDunesObject>(rows * cols) { DesertDunesEmptyObject }
    val emptyOfDunes = mutableListOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: DesertDunesObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: DesertDunesObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = DesertDunesHintObject()
        updateIsSolved()
    }

    override fun setObject(move: DesertDunesGameMove): GameOperationType {
        if (!isValid(move.p) || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: DesertDunesGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is DesertDunesEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) DesertDunesMarkerObject else DesertDunesDuneObject()
            is DesertDunesDuneObject -> if (markerOption == MarkerOptions.MarkerLast) DesertDunesMarkerObject else DesertDunesEmptyObject
            is DesertDunesMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) DesertDunesDuneObject() else DesertDunesEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 17/Desert Dunes

        Summary
        Hide and seek in the desert

        Description
        1. Put some dunes on the desert so that each Oasis dweller can reach the
           number of Oases marked on it.
        2. The desert among dunes (including oases) should be all connected
           horizontally or vertically.
        3. Dwellers can move horizontally or vertically.
        4. Dunes cannot touch each other horizontally or vertically.
        5. No area of desert of 2x2 should be empty of Dunes.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is DesertDunesForbiddenObject)
                    this[r, c] = DesertDunesEmptyObject
        // 5. No area of desert of 2x2 should be empty of Dunes.
        emptyOfDunes.clear()
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val isEmptyOfDunes = DesertDunesGame.offset2.map { p + it }.all { this[it] !is DesertDunesDuneObject }
                if (isEmptyOfDunes) { emptyOfDunes.add(p + Position.SouthEast); isSolved = false }
            }
        // 4. Dunes cannot touch each other horizontally or vertically.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] !is DesertDunesDuneObject) continue
                for (os in DesertDunesGame.offset) {
                    val p2 = p + os
                    if (!isValid(p2)) continue
                    if (this[p2] is DesertDunesDuneObject) {
                        isSolved = false
                        this[p] = DesertDunesDuneObject(AllowedObjectState.Error)
                        this[p2] = DesertDunesDuneObject(AllowedObjectState.Error)
                    } else if (allowedObjectsOnly && this[p2] is DesertDunesEmptyObject)
                        this[p2] = DesertDunesForbiddenObject
                }
            }
        // 2. The desert among dunes (including oases) should be all connected
        //    horizontally or vertically.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] is DesertDunesDuneObject) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node) {
            for (os in GardenerGame.offset) {
                val p2 = p + os
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
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
            DesertDunesGame.offset.map { p + it }
                .filter { isValid(it) && this[it] !is DesertDunesDuneObject }
                .forEach { g.connectNode(pos2node[p]!!, pos2node[it]!!) }
            g.rootNode = pos2node[p]!!
            val nodeList = g.bfs()
            val index = g.nodes.indexOf(pos2node[p]!!)
            g.adjMatrix!![index] = IntArray(g.size) { 0 }
            nodeList
                .map { node -> pos2node.firstNotNullOf { (k, v) -> if (v == node) k else null } }
                .filter { this[it] is DesertDunesHintObject }
                .forEach { hints.add(it) }
            hints.remove(p)
            val n1 = hints.size
            val s = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            this[p] = DesertDunesHintObject(s)
            if (s != HintState.Complete) isSolved = false
        }
    }
}