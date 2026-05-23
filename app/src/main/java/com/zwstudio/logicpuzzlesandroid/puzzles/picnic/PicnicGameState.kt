package com.zwstudio.logicpuzzlesandroid.puzzles.picnic

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PicnicGameState(game: PicnicGame) : CellsGameState<PicnicGame, PicnicGameMove, PicnicGameState>(game) {
    val hint2blanket = mutableMapOf<Position, Position>()
    val blanket2hint = mutableMapOf<Position, Position>()
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

    init {
        updateIsSolved()
    }

    override fun setObject(move: PicnicGameMove): GameOperationType {
        val p = move.p
        val dir = move.dir
        val pHint = blanket2hint[p]
        if (pHint != null && dir == PicnicGame.PUZ_TAP_MOVE) {
            blanket2hint.remove(p)
            hint2blanket.remove(pHint)
        } else if (game.pos2hint[p] != null && dir != PicnicGame.PUZ_TAP_MOVE) {
            // 6. The number on top of the basket shows you how many tiles the basket must
            //    be flung.
            val os = PicnicGame.offset[dir]
            val n = game.pos2hint[p]!!
            var pBlanket = p
            for (i in 0..<n) {
                pBlanket += os
                if (!isValid(pBlanket)) return GameOperationType.Invalid
            }
            if (blanket2hint[pBlanket] != null || game.pos2hint[pBlanket] != null && hint2blanket[pBlanket] == null) return GameOperationType.Invalid
            hint2blanket[p] = pBlanket
            blanket2hint[pBlanket] = p
        } else
            return GameOperationType.Invalid
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
        if (blanket2hint.size != game.pos2hint.size) isSolved = false
        // 4. find a way to lay every picnic basket so that no blanket touches another
        //    one, horizontally or vertically.
        for (p in blanket2hint.keys) {
            val s = if (PicnicGame.offset.all {
                blanket2hint[p + it] == null
            }) AllowedObjectState.Normal else AllowedObjectState.Error
            pos2state[p] = s
            if (s != AllowedObjectState.Normal) isSolved = false
        }
        if (!isSolved) return
        // 5. Also the remaining park should be accessible to everyone, so empty grass
        //    spaces should form a single continuous area.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (blanket2hint[p] != null) continue
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