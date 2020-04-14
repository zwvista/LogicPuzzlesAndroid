package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.data.List
import fj.function.Integers
import java.util.*

class BridgesGameState(game: BridgesGame) : CellsGameState<BridgesGame?, BridgesGameMove?, BridgesGameState?>(game) {
    var objArray: Array<BridgesObject?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: BridgesObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: BridgesObject?) {
        set(p.row, p.col, obj)
    }

    fun switchBridges(move: BridgesGameMove?): Boolean {
        val pFrom = move!!.pFrom
        val pTo = move.pTo
        // 4. Bridges can only run horizontally or vertically.
        if (!(pFrom!!.compareTo(pTo) < 0 && (pFrom.row == pTo!!.row || pFrom.col == pTo.col))) return false
        val o11 = get(pFrom)
        val o22 = get(pTo)
        if (!(o11 is BridgesIslandObject && o22 is BridgesIslandObject)) return false
        val o1 = o11
        val o2 = o22
        val n1 = if (pFrom.row == pTo.row) 1 else 2
        val n2 = (n1 + 2) % 4
        val os: Position = BridgesGame.Companion.offset.get(n1)
        val p = pFrom.add(os)
        while (p != pTo) {
            when (o1.bridges[n1]) {
                0 -> {
                    // 4. Bridges can't cross each other.
                    val o = get(p) as? BridgesEmptyObject ?: return false
                    set(p, BridgesBridgeObject())
                }
                2 -> set(p, BridgesEmptyObject())
            }
            p.addBy(os)
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
        for ((p, info) in game!!.islandsInfo) {
            val o = get(p) as BridgesIslandObject?
            val n1 = fj.data.Array.array(*o!!.bridges).foldLeft(Integers.add, 0)
            val n2 = info.bridges
            o.state = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
            if (!isSolved) continue
            val node = Node(p.toString())
            pos2node[p] = node
            g.addNode(node)
        }
        if (!isSolved) return
        for ((p, info) in game!!.islandsInfo) {
            for (p2 in info.neighbors) {
                if (p2 == null) continue
                g.connectNode(pos2node[p], pos2node[p2])
            }
        }
        // 2. You must connect all the islands with Bridges, making sure every
        // island is connected to each other with a Bridges path.
        g.setRootNode(List.iterableList(pos2node.values).head())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        Arrays.fill(objArray, BridgesEmptyObject())
        for (p in game.islandsInfo.keys) set(p, BridgesIslandObject())
    }
}