package com.zwstudio.logicpuzzlesandroid.puzzles.directionalplanks

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DirectionalPlanksGameState(game: DirectionalPlanksGame) : CellsGameState<DirectionalPlanksGame, DirectionalPlanksGameMove, DirectionalPlanksGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    var woods = mutableSetOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: DirectionalPlanksGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + DirectionalPlanksGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: DirectionalPlanksGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 3/Puzzle Set 5/Directional Planks

        Summary
        Can't move

        Description
        1. Divide the board in areas of three tiles (planks_offset).
        2. Each plank contains one number and the number tells you how many
           directions the Plank can move, when the board is completed.
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
                    if (this[p + DirectionalPlanksGame.offset2[i]][DirectionalPlanksGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + DirectionalPlanksGame.offset[i]]!!)
            }
        woods.clear()
        val planks = mutableListOf<MutableList<Position>>()
        val pos2plank = mutableMapOf<Position, Int>()
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }.toMutableList()
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.nails.contains(it) }
            if (rng.isEmpty()) continue
            // 2. Planks are areas of exactly three cells and can be straight or angled.
            if (area.size != 3) { isSolved = false; continue }
            // 1. Locate some pieces of wood (Planks).
            // 3. Each Plank contains one nail.
            if (rng.size != 1) { isSolved = false; continue }
            val n = planks.size
            planks.add(area)
            for (p in area) {
                pos2plank[p] = n
                woods.add(p)
            }
        }
        // 4. After finding all the Planks, it must be possible to move each piece
        //    by one cell in at least one direction.
        for (plank in planks)
            if (!DirectionalPlanksGame.offset.any { os ->
                val area = plank.map { it + os }
                area.all { plank.contains(it) || !woods.contains(it) }
            }) isSolved = false
    }
}