package com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class FenceItUpGameState(game: FenceItUpGame) : CellsGameState<FenceItUpGame, FenceItUpGameMove, FenceItUpGameState>(game) {
    var objArray: Array<Array<GridLineObject>> = Cloner().deepClone(game.objArray)
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    fun setObject(move: FenceItUpGameMove): Boolean {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + FenceItUpGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty) return false
        val o = this[p1][dir]
        if (o == move.obj) return false
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: FenceItUpGameMove): Boolean {
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
        iOS Game: Logic Games/Puzzle Set 7/Fence It Up

        Summary
        Now with Fences

        Description
        1. A simple puzzle where you have to divide the Board into enclosed
           areas by Fences.
        2. Each area must contain one number and the number tells you the length
           of the perimeter of the area.
    */
    private fun updateIsSolved() {
        isSolved = true
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
                    if (this[p + FenceItUpGame.offset2[i]][FenceItUpGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + FenceItUpGame.offset[i]]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.pos2hint.containsKey(it) }
            if (rng.size != 1) {
                for (p in rng)
                    pos2state[p] = HintState.Normal
                isSolved = false
                continue
            }
            val p2 = rng[0]
            var n1 = 0
            val n2 = game.pos2hint[p2]
            // 2. Each area must contain one number and the number tells you the length
            // of the perimeter of the area.
            for (p in area)
                for (i in 0 until 4)
                    if (this[p + FenceItUpGame.offset2[i]][FenceItUpGame.dirs[i]] == GridLineObject.Line)
                        n1++
            val s = if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p2] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}
