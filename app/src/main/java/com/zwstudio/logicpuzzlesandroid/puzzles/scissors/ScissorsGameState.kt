package com.zwstudio.logicpuzzlesandroid.puzzles.scissors

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ScissorsGameState(game: ScissorsGame) : CellsGameState<ScissorsGame, ScissorsGameMove, ScissorsGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, HintState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    override fun setObject(move: ScissorsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ' ' || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ScissorsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ' ') return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            ' ' -> ScissorsGame.PUZ_BACK_SLASH
            ScissorsGame.PUZ_BACK_SLASH -> ScissorsGame.PUZ_FRONT_SLASH
            ScissorsGame.PUZ_FRONT_SLASH -> ' '
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 1/Scissors

        Summary
        Tailor's puzzle

        Description
        1. Cut the board into patches.
        2. Each patch should contain the numbers 1 to N exactly once (N being the highest number on the board).
        3. Each patch should end on the border.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        var pos2node = mutableMapOf<ScissorsPosition, Node>()
        fun f(sp: ScissorsPosition) {
            val node = Node(sp.toString())
            g.addNode(node)
            pos2node[sp] = node
        }
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                when (this[p]) {
                    ScissorsGame.PUZ_BACK_SLASH -> {
                        f(ScissorsPosition(p, 3)); f(ScissorsPosition(p, 12))
                    }
                    ScissorsGame.PUZ_FRONT_SLASH -> {
                        f(ScissorsPosition(p, 6)); f(ScissorsPosition(p, 9))
                    }
                    else -> f(ScissorsPosition(p, 15))
                }
            }
        for ((sp, node) in pos2node)
            for (i in 0..<4) {
                if (sp.n and (1 shl i) == 0) continue
                val p2 = sp.p + ScissorsGame.offset[i]
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
