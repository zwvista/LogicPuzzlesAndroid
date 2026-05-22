package com.zwstudio.logicpuzzlesandroid.puzzles.bentbridges

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BentBridgesGameState(game: BentBridgesGame) : CellsGameState<BentBridgesGame, BentBridgesGameMove, BentBridgesGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Array<Boolean>) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: BentBridgesGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + BentBridgesGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2)) return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
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
        val islands = mutableSetOf<Position>()
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = get(r, c)
                val dirs = (0..<4).filter { o[it] }
                val cnt = dirs.size
                if (game.pos2hint[p] == null) {
                    if (cnt == 2)
                        // 1. Connect all the islands together with bridges.
                        pos2dirs[p] = dirs
                    else if (cnt != 0)
                        isSolved = false
                } else {
                    pos2state[p] = HintState.Normal
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                    if (cnt != 0) {
                        islands.add(p)
                        pos2dirs[p] = dirs
                    } else
                        // 1. Connect all the islands together with bridges.
                        isSolved = false
                }
            }
        while (islands.isNotEmpty()) {
            val p = islands.first()
            val n2 = game.pos2hint[p]!!
            var n1 = 0
            val dirs = pos2dirs[p]!!
            for (d in dirs) {
                var i = d
                var os = BentBridgesGame.offset[i]
                var p2 = p + os
                var turns = 0
                while (true) {
                    val j = (i + 2) % 4
                    if (game.pos2hint[p2] != null) break
                    var dirs = pos2dirs[p2] ?: break
                    dirs = dirs.filter { it != j }
                    if (dirs.isEmpty()) break
                    val k = dirs[0]
                    if (k != i) {
                        turns++
                        i = k
                    }
                    os = BentBridgesGame.offset[i]
                    p2 += os
                }
                val n3 = game.pos2hint[p2]
                // 4. A bridge can turn once by 90 degrees between islands.
                if (n3 != null && turns < 2) {
                    n1 += 1
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
                } else
                    isSolved = false
            }
            islands.remove(p)
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        // 1. Connect all the islands together with bridges.
        // 2. You should be able to go from any island to any other island in the end.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}
