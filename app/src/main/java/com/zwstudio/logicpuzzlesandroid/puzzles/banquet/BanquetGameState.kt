package com.zwstudio.logicpuzzlesandroid.puzzles.banquet

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class BanquetGameState(game: BanquetGame) : CellsGameState<BanquetGame, BanquetGameMove, BanquetGameState>(game) {
    var hint2blanket = mutableMapOf<Position, Position>()
    var blanket2hint = mutableMapOf<Position, Position>()
    var pos2state = mutableMapOf<Position, AllowedObjectState>()

    init {
        for (p in game.pos2hint.keys) {
            hint2blanket[p] = p
            blanket2hint[p] = p
        }
        updateIsSolved()
    }

    override fun setObject(move: BanquetGameMove): GameOperationType {
        val p = move.p
        val pHint = blanket2hint[p] ?: return GameOperationType.Invalid
        blanket2hint.remove(p)
        if (p != pHint) {
            hint2blanket[pHint] = pHint
            blanket2hint[pHint] = pHint
        } else {
            // 6. The number on top of the basket shows you how many tiles the basket must
            //    be flung.
            val os = BanquetGame.offset[move.dir]
            val n = game.pos2hint[p]!!
            var pBlanket = p
            for (i in 0..<n) {
                pBlanket += os
                if (!isValid(pBlanket)) return GameOperationType.Invalid
            }
            if (blanket2hint[pBlanket] != null) return GameOperationType.Invalid
            hint2blanket[pHint] = pBlanket
            blanket2hint[pBlanket] = pHint
        }
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
        val blankets = mutableSetOf<Position>()
        for ((pBlanket, pHint) in blanket2hint)
            if (pBlanket == pHint)
                isSolved = false
            else
                blankets.add(pBlanket)
        // 4. find a way to lay every picnic basket so that no blanket touches another
        //    one, horizontally or vertically.
        for (p in blankets) {
            val s = if (BanquetGame.offset.all {
                !blankets.contains(p + it)
            }) AllowedObjectState.Normal else AllowedObjectState.Error
            pos2state[p] = s
            if (s != AllowedObjectState.Normal) isSolved = false
        }
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (blankets.contains(p)) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node)
            for (os in BanquetGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}