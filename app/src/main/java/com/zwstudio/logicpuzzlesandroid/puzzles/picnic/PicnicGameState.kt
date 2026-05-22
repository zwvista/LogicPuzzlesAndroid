package com.zwstudio.logicpuzzlesandroid.puzzles.picnic

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PicnicGameState(game: PicnicGame) : CellsGameState<PicnicGame, PicnicGameMove, PicnicGameState>(game) {
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

    override fun setObject(move: PicnicGameMove): GameOperationType {
        val p = move.p
        val pHint = blanket2hint[p] ?: return GameOperationType.Invalid
        blanket2hint.remove(p)
        if (p != pHint) {
            hint2blanket[pHint] = pHint
            blanket2hint[pHint] = pHint
        } else {
            // 6. The number on top of the basket shows you how many tiles the basket must
            //    be flung.
            val os = PicnicGame.offset[move.dir]
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
        iOS Game: 100 Logic Games 3/Puzzle Set 1/Picnic

        Summary
        Fling the Blanket

        Description
        1. As usual, on the day of the National Holiday Picnic, the park is crowded.
        2. You brought your picnic basket (like everyone else) and your blanket (like
           everyone else).
        3. The object is to make space for everyone and to leave the park open for
           walking around.
        4. find a way to lay every picnic basket so that no blanket touches another
           one, horizontally or vertically.
        5. Also the remaining park should be accessible to everyone, so empty grass
           spaces should form a single continuous area.
        6. The number on top of the basket shows you how many tiles the basket must
           be flung.
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
            val s = if (PicnicGame.offset.all {
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
            for (os in PicnicGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}