package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class FenceLitsGameState(game: FenceLitsGame) : CellsGameState<FenceLitsGame, FenceLitsGameMove, FenceLitsGameState>(game) {
    var objArray: Array<Array<GridLineObject>> = Cloner().deepClone(game.objArray)
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    fun setObject(move: FenceLitsGameMove): Boolean {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1.add(FenceLitsGame.offset[dir])
        val o = this[p1][dir]
        if (o == move.obj) return false
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: FenceLitsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p][move.dir]
        move.obj = when (o) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 12/FenceLits

        Summary
        Fencing Tetris

        Description
        1. The goal is to divide the board into Tetris pieces, including the
           square one (differently from LITS).
        2. The number in a cell tells you how many of the sides are marked
           (like slitherlink).
        3. Please consider that the outside border of the board as marked.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 2. The number in a cell tells you how many of the sides are marked
        // (like slitherlink).
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            for (i in 0 until 4)
                if (this[p.add(FenceLitsGame.offset2[i])][FenceLitsGame.dirs[i]] == GridLineObject.Line)
                    n1++
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (n1 != n2) isSolved = false
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                for (i in 0 until 4)
                    if (this[p.add(FenceLitsGame.offset2[i])][FenceLitsGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p.add(FenceLitsGame.offset[i])]!!)
            }
        if (!isSolved) return
        // 1. The goal is to divide the board into Tetris pieces, including the
        // square one (differently from LITS).
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }.sorted()
            if (area.size != 4) {
                isSolved = false
                return
            }
            val treeOffsets = mutableListOf<Position>()
            val p2 = Position(area.map { it.row }.min()!!, area.map { it.col }.min()!!)
            for (p in area)
                treeOffsets.add(p.subtract(p2))
            if (!FenceLitsGame.tetrominoes.any { it == treeOffsets }) {
                isSolved = false
                return
            }
            for (p in area)
                pos2node.remove(p)
        }
    }
}