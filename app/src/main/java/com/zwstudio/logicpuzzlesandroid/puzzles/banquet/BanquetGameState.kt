package com.zwstudio.logicpuzzlesandroid.puzzles.banquet

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BanquetGameState(game: BanquetGame) : CellsGameState<BanquetGame, BanquetGameMove, BanquetGameState>(game) {
    val hint2table = mutableMapOf<Position, Position>()
    val table2hint = mutableMapOf<Position, Position>()
    val tablePath = mutableSetOf<Position>()
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

    init {
        updateIsSolved()
    }

    override fun setObject(move: BanquetGameMove): GameOperationType {
        val p = move.p
        val dir = move.dir
        val pHint = table2hint[p]
        if (pHint != null && dir == BanquetGame.PUZ_TAP_MOVE) {
            table2hint.remove(p)
            hint2table.remove(pHint)
        } else if (game.pos2hint[p] != null) {
            // 2. The number on the table tells you how many tiles it must be moved.
            //    Tables without numbers must stay put.
            var pTable = p
            val n = game.pos2hint[p]!!
            if ((n == 0) != (dir == BanquetGame.PUZ_TAP_MOVE)) return GameOperationType.Invalid
            if (n > 0) {
                val os = BanquetGame.offset[dir]
                for (i in 0..<n) {
                    pTable += os
                    // 3. Tables can't cross other tables, nor cross other tables paths after
                    //    they moved.
                    if (!isValid(pTable) || table2hint[pTable] != null ||
                        game.pos2hint[pTable] != null && hint2table[pTable] == null ||
                        tablePath.contains(pTable))
                        return GameOperationType.Invalid
                }
                pTable = p
                for (i in 0..<n) {
                    pTable += os
                    if (i < n - 1)
                        tablePath.add(p)
                }
            }
            hint2table[p] = pTable
            table2hint[pTable] = p
        } else
            return GameOperationType.Invalid
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 2/Banquet

        Summary
        A table here, please

        Description
        1. Join the tables in order to form "banquets" of at least two tables.
        2. The number on the table tells you how many tiles it must be moved.
           Tables without numbers must stay put.
        3. Tables can't cross other tables, nor cross other tables paths after
           they moved.
        4. Banquets cannot touch each other horizontally or vertically
           (they can touch diagonally).
        5. Banquets can't be L-shaped but can be more than one table wide.
    */
    private fun updateIsSolved() {
        isSolved = true
        if (table2hint.size != game.pos2hint.size) isSolved = false
        // 1. Join the tables in order to form "banquets" of at least two tables.
        // 4. Banquets cannot touch each other horizontally or vertically
        //    (they can touch diagonally).
        // 5. Banquets can't be L-shaped but can be more than one table wide.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (table2hint[p] == null) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node)
            for (os in BanquetGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val banquet = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in banquet)
                pos2node.remove(p)
            val n1 = banquet.size
            var (r1, r2) = rows to 0
            var (c1, c2) = cols to 0
            for (p in banquet) {
                if (r2 < p.row) r2 = p.row
                if (r1 > p.row) r1 = p.row
                if (c2 < p.col) c2 = p.col
                if (c1 > p.col) c1 = p.col
            }
            val rs = r2 - r1 + 1
            val cs = c2 - c1 + 1
            val s = if (rs * cs == n1 && n1 > 1) AllowedObjectState.Normal else AllowedObjectState.Error
            if (s != AllowedObjectState.Normal) isSolved = false
            for (p in banquet)
                pos2state[p] = s
        }
    }
}