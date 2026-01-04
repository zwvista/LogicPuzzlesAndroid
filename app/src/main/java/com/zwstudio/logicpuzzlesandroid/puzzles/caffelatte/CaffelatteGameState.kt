package com.zwstudio.logicpuzzlesandroid.puzzles.caffelatte

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.MasyuGame
import com.zwstudio.logicpuzzlesandroid.puzzles.pouringwater.PouringWaterGame.Companion.offset

class CaffelatteGameState(game: CaffelatteGame) : CellsGameState<CaffelatteGame, CaffelatteGameMove, CaffelatteGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: CaffelatteGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + MasyuGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2)) return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 7/Caffelatte

        Summary
        Cows and Coffee

        Description
        1. Make Cappuccino by linking each cup to one or more coffee beans and cows.
        2. Links must be straight lines, not crossing each other.
        3. To each cup there must be linked an equal number of beans and cows. At
           least one of each.
        4. When linking multiple beans and cows, you can also link cows to cows and
           beans to beans, other than linking them to the cup.
    */
    private fun updateIsSolved() {
        isSolved = true
        val rng = mutableListOf<Position>()
        val ch2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                val ch = game[p]
                val dirs = (0 until 4).filter { o[it] }
                ch2dirs[p] = dirs
                val cnt = dirs.size
                if (ch == ' ') {
                    if (!(cnt == 0 || cnt == 2)) {
                        isSolved = false; return
                    }
                    if (cnt == 2) rng.add(p)
                } else {
                    if (cnt == 0) { isSolved = false; return }
                    rng.add(p)
                }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (p in rng) {
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
        }
        for (p in rng)
            for (i in 0 until 4)
                if (this[p][i])
                    g.connectNode(pos2node[p]!!, pos2node[p + offset[i]]!!)
        while (rng.isNotEmpty()) {
            g.rootNode = pos2node[rng.first()]!!
            val nodeList = g.bfs()
            val area = rng.filter { nodeList.contains(pos2node[it]) }
            rng.removeAll(area)
            val nBean = area.count { game[it] == CaffelatteGame.PUZ_BEAN }
            val nCup = area.count { game[it] == CaffelatteGame.PUZ_CUP }
            val nMilk = area.count { game[it] == CaffelatteGame.PUZ_MILK }
            if (!(nCup == 1 && nBean == nMilk)) {
                isSolved = false; return
            }
        }
    }
}