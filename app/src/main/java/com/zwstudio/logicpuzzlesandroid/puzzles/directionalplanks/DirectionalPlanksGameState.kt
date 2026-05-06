package com.zwstudio.logicpuzzlesandroid.puzzles.directionalplanks

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DirectionalPlanksGameState(game: DirectionalPlanksGame) : CellsGameState<DirectionalPlanksGame, DirectionalPlanksGameMove, DirectionalPlanksGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    var woods = mutableSetOf<Position>()
    var pos2state = mutableMapOf<Position, HintState>()

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
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (game.pos2hint.contains(p))
                    pos2state[p] = HintState.Normal
            }
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                for (i in 0..<4)
                    if (this[p + DirectionalPlanksGame.offset2[i]][DirectionalPlanksGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + DirectionalPlanksGame.offset[i]]!!)
            }
        woods.clear()
        val planks = mutableListOf<MutableList<Position>>()
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }.toMutableList()
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.pos2hint.contains(it) }
            if (rng.isEmpty()) continue
            // 1. Divide the board in areas of three tiles (planks_offset).
            if (area.size != 3 || rng.size != 1) { isSolved = false; continue }
            planks.add(area)
            for (p in area) {
                woods.add(p)
            }
        }
        fun isValidWood(p: Position): Boolean =
            p.row in 0..<rows - 1 && p.col in 0..<cols - 1
        // 2. Each plank contains one number and the number tells you how many
        //    directions the Plank can move, when the board is completed.
        for (plank in planks) {
            val pHint = plank.first { game.pos2hint.keys.contains(it) }
            val n2 = game.pos2hint[pHint]!!
            val n1 = DirectionalPlanksGame.offset.count { os ->
                val area = plank.map { it + os }
                area.all { plank.contains(it) || isValidWood(it) && !woods.contains(it) }
            }
            val s = if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[pHint] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}