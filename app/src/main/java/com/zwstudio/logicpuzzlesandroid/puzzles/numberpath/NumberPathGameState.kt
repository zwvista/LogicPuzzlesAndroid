package com.zwstudio.logicpuzzlesandroid.puzzles.numberpath

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class NumberPathGameState(game: NumberPathGame) : CellsGameState<NumberPathGame, NumberPathGameMove, NumberPathGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Array<Boolean>) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: NumberPathGameMove): Boolean {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p = move.p
        val p2 = p + NumberPathGame.offset[dir]
        if (!isValid(p2)) return false
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return true
    }

    /*
        iOS Game: Logic Games/Puzzle Set 15/Number Path

        Summary
        Tangled, Scrambled Path

        Description
        1. Connect the top left corner (1) to the bottom right corner (N), including
           all the numbers between 1 and N, only once.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val pStart = Position(0, 0)
        val pEnd = Position(rows - 1, cols - 1)
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val n = this[p].filter { it }.size
                if (p == pStart || p == pEnd) {
                    // 1. Connect the top left corner (1) to the bottom right corner (N).
                    if (n != 1) {
                        isSolved = false
                        return
                    }
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                    continue
                }
                when (n) {
                    0 -> {}
                    2 -> {
                        val node = Node(p.toString())
                        g.addNode(node)
                        pos2node[p] = node
                    }
                    else -> {
                        isSolved = false
                        return
                    }
                }
            }
        val nums = mutableSetOf<Int>()
        for (p in pos2node.keys) {
            val o = this[p]
            nums.add(game[p])
            for (i in 0 until 4) {
                if (!o[i]) continue
                val p2 = p + NumberPathGame.offset[i]
                g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        }
        // 1. Connect the top left corner (1) to the bottom right corner (N), including
        // all the numbers between 1 and N, only once.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        val n1 = game[pEnd]
        val n2 = nums.size
        val n3 = nodeList.size
        val n4 = pos2node.size
        if (n1 != n2 || n1 != n3 || n1 != n4) isSolved = false
    }
}