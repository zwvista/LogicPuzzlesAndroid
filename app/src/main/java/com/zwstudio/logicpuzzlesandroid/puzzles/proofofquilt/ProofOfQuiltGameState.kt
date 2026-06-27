package com.zwstudio.logicpuzzlesandroid.puzzles.proofofquilt

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ProofOfQuiltGameState(game: ProofOfQuiltGame) : CellsGameState<ProofOfQuiltGame, ProofOfQuiltGameMove, ProofOfQuiltGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, HintState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    override fun setObject(move: ProofOfQuiltGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ' ' || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ProofOfQuiltGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ' ') return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            ' ' -> ProofOfQuiltGame.PUZ_BACK_SLASH
            ProofOfQuiltGame.PUZ_BACK_SLASH -> ProofOfQuiltGame.PUZ_FRONT_SLASH
            ProofOfQuiltGame.PUZ_FRONT_SLASH -> ' '
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 8/Proof of Quilt

        Summary
        Quilt the board, following the hints

        Description
         1. The goal is to place triangles in some cells in the end generating a pattern
            similar to a Quilt.
         2. The numbered tiles tell you how many triangles share an edge with it,
            horizontally and vertically
         3, For example, if a tile says 4, it has triangles all around it.
         4. If a tile says 1, it has only one triangle somewhere.
         5. Some tiles will remain blank and will form, along with the triangles, rectangles
            and squares.
         6. These can be tilted by 45 degrees.
         7. Some other tiles are filled but contain no number. These and the hints are
            the only tiles that can be completely filled.
         8. Rectangles or squares can't touch orthogonally, but can touch diagonally
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        var pos2node = mutableMapOf<ProofOfQuiltPosition, Node>()
        fun f(sp: ProofOfQuiltPosition) {
            val node = Node(sp.toString())
            g.addNode(node)
            pos2node[sp] = node
        }
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                when (this[p]) {
                    ProofOfQuiltGame.PUZ_BACK_SLASH -> {
                        f(ProofOfQuiltPosition(p, 3)); f(ProofOfQuiltPosition(p, 12))
                    }
                    ProofOfQuiltGame.PUZ_FRONT_SLASH -> {
                        f(ProofOfQuiltPosition(p, 6)); f(ProofOfQuiltPosition(p, 9))
                    }
                    else -> f(ProofOfQuiltPosition(p, 15))
                }
            }
        for ((sp, node) in pos2node)
            for (i in 0..<4) {
                if (sp.n and (1 shl i) == 0) continue
                val p2 = sp.p + ProofOfQuiltGame.offset[i]
                val j = (i + 2) % 4
                val sp2 = pos2node.keys.firstOrNull { it.p == p2 && it.n and (1 shl j) != 0 } ?: continue
                g.connectNode(node, pos2node[sp2]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }.filter { it.n == 15 }.map { it.p }
            pos2node = pos2node.filter { !nodeList.contains(it.value) }.toMutableMap()
            val num2rng = mutableMapOf<Char, MutableList<Position>>()
            for (p in area) {
                val ch = this[p]
                if (ch.isDigit())
                    num2rng.getOrPut(ch) { mutableListOf() }.add(p)
            }
            val n = num2rng.values.firstOrNull()?.size ?: 0
            val hasNumbers = num2rng.keys.sorted() == game.numbers && num2rng.all { it.value.size == n }
            val s = if (!hasNumbers) HintState.Error else if (n == 1) HintState.Complete else HintState.Normal
            if (s != HintState.Complete) isSolved = false
            for (p in area)
                pos2state[p] = s
        }
    }
}
