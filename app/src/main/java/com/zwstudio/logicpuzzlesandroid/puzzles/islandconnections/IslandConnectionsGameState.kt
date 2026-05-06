package com.zwstudio.logicpuzzlesandroid.puzzles.islandconnections

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class IslandConnectionsGameState(game: IslandConnectionsGame) : CellsGameState<IslandConnectionsGame, IslandConnectionsGameMove, IslandConnectionsGameState>(game) {
    var objArray = Array<IslandConnectionsObject>(rows * cols) { IslandConnectionsEmptyObject }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: IslandConnectionsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: IslandConnectionsObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.islandsInfo.keys)
            this[p] = IslandConnectionsIslandObject()
    }

    fun switchIslandConnections(move: IslandConnectionsGameMove): GameOperationType {
        val pFrom = move.pFrom
        val pTo = move.pTo
        // 4. IslandConnections can only run horizontally or vertically.
        if (!(pFrom < pTo && (pFrom.row == pTo.row || pFrom.col == pTo.col))) return GameOperationType.Invalid
        val o1 = this[pFrom]
        val o2 = this[pTo]
        if (!(o1 is IslandConnectionsIslandObject && o2 is IslandConnectionsIslandObject)) return GameOperationType.Invalid
        val n1 = if (pFrom.row == pTo.row) 1 else 2
        val n2 = (n1 + 2) % 4
        val os = IslandConnectionsGame.offset[n1]
        var p = pFrom + os
        while (p != pTo) {
            when (o1.bridges[n1]) {
                0 -> {
                    // 4. IslandConnections can't cross each other.
                    if (this[p] !is IslandConnectionsEmptyObject) return GameOperationType.Invalid
                    this[p] = IslandConnectionsBridgeObject
                }
                1 -> this[p] = IslandConnectionsEmptyObject
            }
            p += os
        }
        // 5. Lastly, you can connect two islands with either one or two IslandConnections
        // (or none, of course)
        val n = (o1.bridges[n1] + 1) % 2
        o2.bridges[n2] = n
        o1.bridges[n1] = o2.bridges[n2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 5/Island Connections

        Summary
        Simpler Bridges

        Description
        1. Connect the islands with bridges.
        2. All the islands must be connected between them, forming a single path.
        3. Bridges are horizontal or vertical straight lines and cannot cross each other.
        4. Islands with numbers tell you how many bridges depart from it.
        5. Islands without a number can have any number of bridges.
        6. Shaded islands cannot be connected and should be avoided.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        // 3. The number on each island tells you how many IslandConnections are touching
        // that island.
        for ((p, info) in game.islandsInfo) {
            val o = this[p] as IslandConnectionsIslandObject
            val n1 = o.bridges.sum()
            val n2 = info.bridges
            val isUnknown = n2 == IslandConnectionsGame.PUZ_UNKNOWN
            o.state = if (isUnknown && n1 == 0 || !isUnknown && n1 < n2) HintState.Normal else if (isUnknown || n1 == n2) HintState.Complete else HintState.Error
            if (o.state != HintState.Complete) isSolved = false
            if (!isSolved) continue
            val node = Node(p.toString())
            pos2node[p] = node
            g.addNode(node)
        }
        if (!isSolved) return
        for ((p, info) in game.islandsInfo) {
            val o = this[p] as IslandConnectionsIslandObject
            for (i in 0..<4) {
                val p2 = info.neighbors[i]
                if (p2 == null || o.bridges[i] == 0) continue
                g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        }
        // 2. You must connect all the islands with IslandConnections, making sure every
        // island is connected to each other with a IslandConnections path.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}
