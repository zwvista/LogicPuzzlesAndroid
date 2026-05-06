package com.zwstudio.logicpuzzlesandroid.puzzles.planks

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PlanksGameState(game: PlanksGame) : CellsGameState<PlanksGame, PlanksGameMove, PlanksGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    var pos2orient = mutableMapOf<Position, Boolean>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: PlanksGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + PlanksGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PlanksGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[move.p][move.dir]) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 16/Planks

        Summary
        Planks and Nails

        Description
        1. On the board there are a few nails. Each one nails a plank to
           the board.
        2. Planks are 3 tiles long and can be oriented vertically or
           horizontally. The Nail can be in any of the 3 tiles.
        3. Each Plank touches orthogonally exactly two other Planks.
        4. All the Planks form a ring, or a closed loop.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                for (i in 0..<4)
                    if (this[p + PlanksGame.offset2[i]][PlanksGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + PlanksGame.offset[i]]!!)
            }
        pos2orient.clear()
        val planks = mutableListOf<MutableList<Position>>()
        val pos2plank = mutableMapOf<Position, Int>()
        val g2 = Graph()
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }.toMutableList()
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.nails.contains(it) }
            if (rng.isEmpty()) continue
            // 2. Planks are 3 tiles long.
            if (area.size != 3) { isSolved = false; continue }
            area.sort()
            val (os1, os2) = area[1] - area[0] to area[2] - area[1]
            // 2. Planks can be oriented vertically or horizontally.
            if (!(os1 == os2 && (os1 == PlanksGame.offset[1] || os1 == PlanksGame.offset[2]))) { isSolved = false; continue }
            // 1. On the board there are a few nails. Each one nails a plank to
            //    the board.
            // 2. The Nail can be in any of the 3 tiles.
            if (rng.size != 1) { isSolved = false; continue }
            val n = planks.size
            planks.add(area)
            for (p in area) {
                pos2plank[p] = n
                pos2orient[p] = os1 == PlanksGame.offset[1]
            }
            val node = Node(n.toString())
            g2.addNode(node)
        }
        for ((i, plank) in planks.withIndex()) {
            val neighbors = mutableSetOf<Int>()
            for (p in plank)
                for (os in PlanksGame.offset) {
                    val n = pos2plank[p + os]
                    if (n == null || n == i) {continue}
                    neighbors.add(n)
                }
            // 3. Each Plank touches orthogonally exactly two other Planks.
            if (neighbors.size != 2) { isSolved = false; return }
            for (n in neighbors) { g2.connectNode(g2.nodes[i], g2.nodes[n]) }
        }
        if (!isSolved) return
        // 4. All the Planks form a ring, or a closed loop.
        g2.rootNode = g2.nodes[0]
        val nodeList = g2.bfs()
        if (nodeList.size != g2.nodes.size) isSolved = false
    }
}