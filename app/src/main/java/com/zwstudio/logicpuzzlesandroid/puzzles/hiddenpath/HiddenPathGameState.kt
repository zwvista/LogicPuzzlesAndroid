package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenpath

import com.zwstudio.logicpuzzlesandroid.common.domain.*

class HiddenPathGameState(game: HiddenPathGame) : CellsGameState<HiddenPathGame, HiddenPathGameMove, HiddenPathGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: HiddenPathGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 3/Hidden Path

        Summary
        Jump once on every tile, following the arrows

        Description
        Starting at the tile number 1, reach the last tile by jumping from tile to tile.
        1. When jumping from a tile, you have to follow the direction of the arrow and
           land on a tile in that direction
        2. Although you have to follow the direction of the arrow, you can land on any
           tile in that direction, not just the one next to the current tile.
        3. The goal is to jump on every tile, only once and reach the last tile.
    */
    private fun updateIsSolved() {
//        isSolved = true
//        val g = Graph()
//        val pos2node = mutableMapOf<Position, Node>()
//        val pStart = Position(0, 0)
//        val pEnd = Position(rows - 1, cols - 1)
//        for (r in 0 until rows)
//            for (c in 0 until cols) {
//                val p = Position(r, c)
//                val n = this[p].filter { it }.size
//                if (p == pStart || p == pEnd) {
//                    // 1. Connect the top left corner (1) to the bottom right corner (N).
//                    if (n != 1) {
//                        isSolved = false
//                        return
//                    }
//                    val node = Node(p.toString())
//                    g.addNode(node)
//                    pos2node[p] = node
//                    continue
//                }
//                when (n) {
//                    0 -> {}
//                    2 -> {
//                        val node = Node(p.toString())
//                        g.addNode(node)
//                        pos2node[p] = node
//                    }
//                    else -> {
//                        isSolved = false
//                        return
//                    }
//                }
//            }
//        val nums = mutableSetOf<Int>()
//        for (p in pos2node.keys) {
//            val o = this[p]
//            nums.add(game[p])
//            for (i in 0 until 4) {
//                if (!o[i]) continue
//                val p2 = p + HiddenPathGame.offset[i]
//                g.connectNode(pos2node[p]!!, pos2node[p2]!!)
//            }
//        }
//        // 1. Connect the top left corner (1) to the bottom right corner (N), including
//        // all the numbers between 1 and N, only once.
//        g.rootNode = pos2node.values.first()
//        val nodeList = g.bfs()
//        val n1 = game[pEnd]
//        val n2 = nums.size
//        val n3 = nodeList.size
//        val n4 = pos2node.size
//        if (n1 != n2 || n1 != n3 || n1 != n4) isSolved = false
    }
}
