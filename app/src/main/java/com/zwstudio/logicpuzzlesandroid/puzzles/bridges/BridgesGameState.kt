package com.zwstudio.logicpuzzlesandroid.puzzles.bridges

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class BridgesGameState(game: BridgesGame) : CellsGameState<BridgesGame, BridgesGameMove, BridgesGameState>(game) {
    var objArray = Array<BridgesObject>(rows * cols) { BridgesEmptyObject }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: BridgesObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: BridgesObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.islandsInfo.keys)
            this[p] = BridgesIslandObject()
    }

    fun switchBridges(move: BridgesGameMove): Boolean {
        val pFrom = move.pFrom
        val pTo = move.pTo
        // 4. Bridges can only run horizontally or vertically.
        if (!(pFrom < pTo && (pFrom.row == pTo.row || pFrom.col == pTo.col))) return false
        val o1 = this[pFrom]
        val o2 = this[pTo]
        if (!(o1 is BridgesIslandObject && o2 is BridgesIslandObject)) return false
        val n1 = if (pFrom.row == pTo.row) 1 else 2
        val n2 = (n1 + 2) % 4
        val os = BridgesGame.offset[n1]
        var p = pFrom + os
        while (p != pTo) {
            when (o1.bridges[n1]) {
                0 -> {
                    // 4. Bridges can't cross each other.
                    if (this[p] !is BridgesEmptyObject) return false
                    this[p] = BridgesBridgeObject
                }
                2 -> this[p] = BridgesEmptyObject
            }
            p += os
        }
        // 5. Lastly, you can connect two islands with either one or two Bridges
        // (or none, of course)
        val n = (o1.bridges[n1] + 1) % 3
        o2.bridges[n2] = n
        o1.bridges[n1] = o2.bridges[n2]
        updateIsSolved()
        return true
    }

    /*
        iOS Game: Logic Games/Puzzle Set 7/Bridges

        Summary
        Enough Sudoku, let's build!

        Description
        1. The board represents a Sea with some islands on it.
        2. You must connect all the islands with Bridges, making sure every
           island is connected to each other with a Bridges path.
        3. The number on each island tells you how many Bridges are touching
           that island.
        4. Bridges can only run horizontally or vertically and can't cross
           each other.
        5. Lastly, you can connect two islands with either one or two Bridges
           (or none, of course)
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        // 3. The number on each island tells you how many Bridges are touching
        // that island.
        for ((p, info) in game.islandsInfo) {
            val o = this[p] as BridgesIslandObject
            val n1 = o.bridges.sum()
            val n2 = info.bridges
            o.state = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
            if (!isSolved) continue
            val node = Node(p.toString())
            pos2node[p] = node
            g.addNode(node)
        }
        if (!isSolved) return
        for ((p, info) in game.islandsInfo) {
            for (p2 in info.neighbors) {
                if (p2 == null) continue
                g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        }
        // 2. You must connect all the islands with Bridges, making sure every
        // island is connected to each other with a Bridges path.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}
