package com.zwstudio.logicpuzzlesandroid.puzzles.bentbridges

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BentBridgesGameState(game: BentBridgesGame) : CellsGameState<BentBridgesGame, BentBridgesGameMove, BentBridgesGameState>(game) {
    var objArray = Array<BentBridgesObject>(rows * cols) { BentBridgesEmptyObject }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: BentBridgesObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: BentBridgesObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.islandsInfo.keys)
            this[p] = BentBridgesIslandObject()
    }

    fun switchBentBridges(move: BentBridgesGameMove): GameOperationType {
        val pFrom = move.pFrom
        val pTo = move.pTo
        // 4. BentBridges can only run horizontally or vertically.
        if (!(pFrom < pTo && (pFrom.row == pTo.row || pFrom.col == pTo.col))) return GameOperationType.Invalid
        val o1 = this[pFrom]
        val o2 = this[pTo]
        if (!(o1 is BentBridgesIslandObject && o2 is BentBridgesIslandObject)) return GameOperationType.Invalid
        val n1 = if (pFrom.row == pTo.row) 1 else 2
        val n2 = (n1 + 2) % 4
        val os = BentBridgesGame.offset[n1]
        var p = pFrom + os
        while (p != pTo) {
            when (o1.bridges[n1]) {
                0 -> {
                    // 4. BentBridges can't cross each other.
                    if (this[p] !is BentBridgesEmptyObject) return GameOperationType.Invalid
                    this[p] = BentBridgesBridgeObject
                }
                2 -> this[p] = BentBridgesEmptyObject
            }
            p += os
        }
        // 5. Lastly, you can connect two islands with either one or two BentBridges
        // (or none, of course)
        val n = (o1.bridges[n1] + 1) % 3
        o2.bridges[n2] = n
        o1.bridges[n1] = o2.bridges[n2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 1/Bent Bridges

        Summary
        One turn at most

        Description
        1. Connect all the islands together with bridges.
        2. You should be able to go from any island to any other island in the end.
        3. The number on the island tells you how many bridges connect to that island.
        4. A bridge can turn once by 90 degrees between islands.
        5. Bridges cannot cross each other.

        Variants
        6. Crossing: bridges can cross each other, but cannot turn at the intersection.
        7. Magnetic: islands with the same number cannot have a bridge between themselves.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        // 3. The number on each island tells you how many BentBridges are touching
        // that island.
        for ((p, info) in game.islandsInfo) {
            val o = this[p] as BentBridgesIslandObject
            val n1 = o.bridges.sum()
            val n2 = info.bridges
            o.state = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (o.state != HintState.Complete) isSolved = false
            if (!isSolved) continue
            val node = Node(p.toString())
            pos2node[p] = node
            g.addNode(node)
        }
        if (!isSolved) return
        for ((p, info) in game.islandsInfo) {
            val o = this[p] as BentBridgesIslandObject
            for (i in 0 until 4) {
                val p2 = info.neighbors[i]
                if (p2 == null || o.bridges[i] == 0) continue
                g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        }
        // 2. You must connect all the islands with BentBridges, making sure every
        // island is connected to each other with a BentBridges path.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}
