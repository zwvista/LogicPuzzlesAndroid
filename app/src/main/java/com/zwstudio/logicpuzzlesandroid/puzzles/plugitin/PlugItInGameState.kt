package com.zwstudio.logicpuzzlesandroid.puzzles.plugitin

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.MasyuGame

class PlugItInGameState(game: PlugItInGame) : CellsGameState<PlugItInGame, PlugItInGameMove, PlugItInGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: PlugItInGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + MasyuGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2)) return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 6/Plug it in

        Summary
        Give them light

        Description
        1. Connect each battery with a lightbulb by a horizontal or vertical cable.
        2. Cables are not allowed to cross other cables.
    */
    private fun updateIsSolved() {
        isSolved = true
        val rng = mutableListOf<Position>()
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val dirs = (0 until 4).filter { this[p][it] }
                pos2dirs[p] = dirs
                val cnt = dirs.size
                if (game[p] == ' ') {
                    // 2. Cables are not allowed to cross other cables.
                    if (!(cnt == 0 || cnt == 2 && (dirs[0] + 2) % 4 == dirs[1])) {
                        isSolved = false; return
                    }
                    if (cnt == 2) rng.add(p)
                } else {
                    if (cnt != 1) { isSolved = false; return }
                    rng.add(p)
                }
            }
        // 1. Connect each battery with a lightbulb by a horizontal or vertical cable.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (p in rng) {
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
        }
        for (p in rng)
            for (i in 0 until 4)
                if (this[p][i]) {
                    val node2 = pos2node[p + PlugItInGame.offset[i]]
                    if (node2 == null) { isSolved = false; return }
                    g.connectNode(pos2node[p]!!, node2)
                }
        while (rng.isNotEmpty()) {
            g.rootNode = pos2node[rng.first()]!!
            val nodeList = g.bfs()
            val area = rng.filter { nodeList.contains(pos2node[it]) }
            rng.removeAll(area)
            val nLightBulb = area.count { game[it] == PlugItInGame.PUZ_LIGHTBULB }
            val nBattery = area.count { game[it] == PlugItInGame.PUZ_BATTERY }
            if (!(nLightBulb == 1 && nBattery == 1)) {
                isSolved = false; return
            }
        }
    }
}