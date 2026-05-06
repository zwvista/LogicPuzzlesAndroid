package com.zwstudio.logicpuzzlesandroid.puzzles.onlybends

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class OnlyBendsGameState(game: OnlyBendsGame) : CellsGameState<OnlyBendsGame, OnlyBendsGameMove, OnlyBendsGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: OnlyBendsGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + OnlyBendsGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2))
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 1/Only Bends

        Summary
        We don't like long straights

        Description
        1. Connect pairs of houses with roads that can't go straight! :)
        2. Each house must be connected with another house. The road connecting
           them can't have straights, but has to turn on every tile it passes through.
        3. Roads cannot cross and cannot go over other houses.
        4. The entire board must be filled with roads! (asphalt lobby rule)
    */
    private fun updateIsSolved() {
        isSolved = true
        val rng = mutableListOf<Position>()
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                rng.add(p)
                val dirs = (0..<4).filter { this[p][it] }
                pos2dirs[p] = dirs
                val cnt = dirs.size
                if (game[p] == ' ') {
                    // 2. The road connecting
                    //    them can't have straights, but has to turn on every tile it passes through.
                    // 4. The entire board must be filled with roads! (asphalt lobby rule)
                    if (!(cnt == 2 && (dirs[0] + 2) % 4 != dirs[1])){
                        isSolved = false; return
                    }
                } else {
                    // 3. Roads cannot cross and cannot go over other houses.
                    if (cnt != 1) { isSolved = false; return }
                }
            }
        // 2. Each house must be connected with another house.
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
                    val node2 = pos2node[p + OnlyBendsGame.offset[i]]
                    if (node2 == null) { isSolved = false; return }
                    g.connectNode(pos2node[p]!!, node2)
                }
        while (rng.isNotEmpty()) {
            g.rootNode = pos2node[rng.first()]!!
            val nodeList = g.bfs()
            val area = rng.filter { nodeList.contains(pos2node[it]) }
            rng.removeAll(area)
            val nHouse = area.count { game[it] != ' ' }
            if (nHouse != 2) {
                isSolved = false; return
            }
        }
    }
}