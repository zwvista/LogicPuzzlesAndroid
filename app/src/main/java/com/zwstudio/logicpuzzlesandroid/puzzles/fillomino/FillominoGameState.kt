package com.zwstudio.logicpuzzlesandroid.puzzles.fillomino

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FillominoGameState(game: FillominoGame) : CellsGameState<FillominoGame, FillominoGameMove, FillominoGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FillominoGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ' ' || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FillominoGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ' ') return GameOperationType.Invalid
        val o = this[p]
        move.obj = if (o == ' ') '1' else if (o == game.chMax) ' ' else o + 1
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 3/Fillomino

        Summary
        Detect areas marked by their extension

        Description
        1. The goal is to detect areas marked with the tile count of the area
           itself.
        2. So for example, areas marked '1', will always consist of one single
           tile. Areas marked with '2' will consist of two (horizontally or
           vertically) adjacent tiles. Tiles numbered '3' will appear in a group
           of three and so on.
        3. Two areas with the same number can't be horizontally or vertically touching.
        4. Lastly, please note that some areas can also be totally hidden at the start.
           In the example you can see a '1' which wasn't hinted in the initial setup.

        Variation
        5. Fillomino has several variants.
        6. No Rectangles: Areas can't form Rectangles.
        7. Only Rectangles: Areas can ONLY form Rectangles.
        8. Non Consecutive: Areas can't touch another area which has +1 or -1
           as number (orthogonally).
        9. Consecutive: Areas MUST touch another area which has +1 or -1
           as number (orthogonally).
        10.No Row or Column Repeats: Different areas with the same number
           can't appear in the same column or row.
        11.All Odds: There are only odd numbers on the board.
        12.All Evens: There are only even numbers on the board.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
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
            for (os in FillominoGame.offset) {
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