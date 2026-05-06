package com.zwstudio.logicpuzzlesandroid.puzzles.gardentunnels

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class GardenTunnelsGameState(game: GardenTunnelsGame) : CellsGameState<GardenTunnelsGame, GardenTunnelsGameMove, GardenTunnelsGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: GardenTunnelsGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + GardenTunnelsGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2))
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 4/Garden Tunnels

        Summary
        Whack a mole

        Description
        1. the board represents a few gardens where some moles are digging
           straight line tunnels.
        2. Each tunnel starts in the garden and ends in a different garden,
           and can pass through other gardens.
        3. The number in the garden tells you how many tunnels start/end in that
           garden.
        4. The entire board must be filled with tunnels.
    */
    private fun updateIsSolved() {
        isSolved = true
        val rng = mutableListOf<Position>()
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dirs = (0..<4).filter { this[p][it] }
                pos2dirs[p] = dirs
                val cnt = dirs.size
                if (cnt == 1 || cnt == 2) {
                    rng.add(p)
                    if (cnt == 2)
                        // 1. the board represents a few gardens where some moles are digging
                        //    straight line tunnels.
                        if (dirs[1] - dirs[0] != 2) isSolved = false
                } else
                    // 4. The entire board must be filled with tunnels.
                    isSolved = false
            }
        // 3. The number in the garden tells you how many tunnels start/end in that
        //    garden.
        for (area in game.areas) {
            val pHint = area.firstOrNull { game.pos2hint[it] != null } ?: continue
            val n2 = game.pos2hint[pHint]!!
            val n1 = area.count { pos2dirs[it]!!.size == 1 }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            pos2state[pHint] = s
        }
        // 3. The number in the garden tells you how many tunnels start/end in that
        //    garden.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (p in rng) {
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
        }
        for (p in rng)
            for (i in 0..<4)
                if (this[p][i]) {
                    val node2 = pos2node[p + GardenTunnelsGame.offset[i]]
                    if (node2 == null) { isSolved = false; return }
                    g.connectNode(pos2node[p]!!, node2)
                }
        while (rng.isNotEmpty()) {
            g.rootNode = pos2node[rng.first()]!!
            val nodeList = g.bfs()
            val tunnel = rng.filter { nodeList.contains(pos2node[it]) }
            rng.removeAll(tunnel)
            if (tunnel.count { pos2dirs[it]!!.isNotEmpty() } < 2) { isSolved = false; return }
        }
    }
}