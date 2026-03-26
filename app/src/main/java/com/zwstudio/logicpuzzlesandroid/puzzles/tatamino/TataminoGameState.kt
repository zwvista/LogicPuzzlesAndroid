package com.zwstudio.logicpuzzlesandroid.puzzles.tatamino

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TataminoGameState(game: TataminoGame) : CellsGameState<TataminoGame, TataminoGameMove, TataminoGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: TataminoGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ' ' || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TataminoGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ' ') return GameOperationType.Invalid
        val o = this[p]
        move.obj = if (o == ' ') '1' else if (o == '3') ' ' else o + 1
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/Tatamino

        Summary
        Which is a little Tatami

        Description
        1. Plays like Fillomino, in which you have to guess areas on the board
           marked by their number.
        2. In Tatamino, however, you only have areas 1, 2 and 3 tiles big.
        3. Please remember two areas of the same number size can't be touching.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] == ' ')
                    isSolved = false
                else {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            val ch = this[p]
            for (os in TataminoGame.offset) {
                val p2 = p + os
                if (isValid(p2) && this[p2] == ch)
                    g.connectNode(node, pos2node[p2]!!)
            }
        }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val ch = this[area[0]]
            val n1 = area.size
            val n2 = ch - '0'
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            for (p in area) { pos2state[p] = s }
            if (s != HintState.Complete) isSolved = false
        }
    }
}