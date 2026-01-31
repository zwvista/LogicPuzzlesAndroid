package com.zwstudio.logicpuzzlesandroid.puzzles.flowerbedshrubs

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FlowerbedShrubsGameState(game: FlowerbedShrubsGame) : CellsGameState<FlowerbedShrubsGame, FlowerbedShrubsGameMove, FlowerbedShrubsGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    var pos2state = mutableMapOf<Position, HintState>()
    var shrubs = mutableSetOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: FlowerbedShrubsGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + FlowerbedShrubsGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FlowerbedShrubsGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 3/Puzzle Set 2/Flowerbed Shrubs

        Summary
        A lively garden

        Description
        1. Divide the board in Flowerbeds of exactly three tiles. Each Flowerbed
           contains a number.
        2. Single tiles left outside Flowerbeds are Shrubs. Shrubs cannot touch
           each other orthogonally.
        3. The number on each Flowerbed tells you how many Shrubs are adjacent to it.
    */
    private fun updateIsSolved() {
        isSolved = true
        val flowerbeds = mutableListOf<List<Position>>()
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
                    if (this[p + FlowerbedShrubsGame.offset2[i]][FlowerbedShrubsGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + FlowerbedShrubsGame.offset[i]]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rng = area.filter { game.pos2hint.containsKey(it) }
            // 2. Each Box must contain one number.
            // 1. Divide the board in Flowerbeds of exactly three tiles. Each Flowerbed
            //    contains a number.
            val cnt = area.size
            if (rng.isEmpty()) {
                if (cnt == 1)
                    shrubs.add(area[0])
                else
                    isSolved = false
            } else if (rng.size > 1 || cnt != 3) {
                for (p in rng)
                    pos2state[p] = HintState.Normal
                isSolved = false
            } else
                flowerbeds.add(area)
        }
        // 3. The number on each Flowerbed tells you how many Shrubs are adjacent to it.
        for (area in flowerbeds) {
            val pHint = area.first { game.pos2hint[it] != null }
            val n1 = game.pos2hint[pHint]!!
            val shrubs2 = area
                .flatMap { p -> FlowerbedShrubsGame.offset.map { p + it } }
                .filter { shrubs.contains(it) }
                .toSet()
            val n2 = shrubs2.size
            val s = if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[pHint] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}