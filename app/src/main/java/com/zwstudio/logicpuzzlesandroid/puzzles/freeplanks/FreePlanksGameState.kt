package com.zwstudio.logicpuzzlesandroid.puzzles.freeplanks

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FreePlanksGameState(game: FreePlanksGame) : CellsGameState<FreePlanksGame, FreePlanksGameMove, FreePlanksGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    var pos2orient = mutableMapOf<Position, Boolean>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: FreePlanksGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + FreePlanksGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FreePlanksGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 2/Puzzle Set 6/Free Planks

        Summary
        Nail slavery

        Description
        1. Locate some pieces of wood (Planks).
        2. Planks are areas of exactly three cells and can be straight or angled.
        3. Each Plank contains one nail.
        4. After finding all the Planks, it must be possible to move each piece
           by one cell in at least one direction.
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
                    if (this[p + FreePlanksGame.offset2[i]][FreePlanksGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + FreePlanksGame.offset[i]]!!)
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
            // 2. FreePlanks are 3 tiles long.
            if (area.size != 3) { isSolved = false; continue }
            area.sort()
            val (os1, os2) = area[1] - area[0] to area[2] - area[1]
            // 2. FreePlanks can be oriented vertically or horizontally.
            if (!(os1 == os2 && (os1 == FreePlanksGame.offset[1] || os1 == FreePlanksGame.offset[2]))) { isSolved = false; continue }
            // 1. On the board there are a few nails. Each one nails a plank to
            //    the board.
            // 2. The Nail can be in any of the 3 tiles.
            if (rng.size != 1) { isSolved = false; continue }
            val n = planks.size
            planks.add(area)
            for (p in area) {
                pos2plank[p] = n
                pos2orient[p] = os1 == FreePlanksGame.offset[1]
            }
            val node = Node(n.toString())
            g2.addNode(node)
        }
        for ((i, plank) in planks.withIndex()) {
            val neighbors = mutableSetOf<Int>()
            for (p in plank)
                for (os in FreePlanksGame.offset) {
                    val n = pos2plank[p + os]
                    if (n == null || n == i) {continue}
                    neighbors.add(n)
                }
            // 3. Each Plank touches orthogonally exactly two other FreePlanks.
            if (neighbors.size != 2) { isSolved = false; return }
            for (n in neighbors) { g2.connectNode(g2.nodes[i], g2.nodes[n]) }
        }
        if (!isSolved) return
        // 4. All the FreePlanks form a ring, or a closed loop.
        g2.rootNode = g2.nodes[0]
        val nodeList = g2.bfs()
        if (nodeList.size != g2.nodes.size) isSolved = false
    }
}