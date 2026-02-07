package com.zwstudio.logicpuzzlesandroid.puzzles.pondsandflowerbeds

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PondsAndFlowerbedsGameState(game: PondsAndFlowerbedsGame) : CellsGameState<PondsAndFlowerbedsGame, PondsAndFlowerbedsGameMove, PondsAndFlowerbedsGameState>(game) {
    var objArray: MutableList<MutableList<GridLineObject>> = Cloner().deepClone(game.objArray)
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var shrubs = mutableSetOf<Position>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    override fun setObject(move: PondsAndFlowerbedsGameMove): GameOperationType {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + PondsAndFlowerbedsGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return GameOperationType.Invalid
        val o = this[p1][dir]
        if (o == move.obj) return GameOperationType.Invalid
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PondsAndFlowerbedsGameMove): GameOperationType {
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
        iOS Game: 100 Logic Games 2/Puzzle Set 3/Ponds and Flowerbeds

        Summary
        Mad Gardener!

        Description
        1. The aim is to locate some Flowerbeds and Ponds in the field.
        2. A Flowerbed is an area of 3 cells, containing one flower.
        3. A Pond is an area of any size without flower.
        4. Each 2x2 area must contain at least a Hedge or a Pond.
        5. Hedges when presents, are given in light green.
    */
    private fun updateIsSolved() {
        isSolved = true
        pos2stateHint.clear()
        pos2stateAllowed.clear()
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
                    if (this[p + PondsAndFlowerbedsGame.offset2[i]][PondsAndFlowerbedsGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + PondsAndFlowerbedsGame.offset[i]]!!)
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
                    pos2stateHint[p] = HintState.Normal
                isSolved = false
            } else
                flowerbeds.add(area)
        }
        // 2. Single tiles left outside Flowerbeds are Shrubs. Shrubs cannot touch
        //    each other orthogonally.
        for (p in shrubs) {
            val rng = PondsAndFlowerbedsGame.offset.map { p + it }.filter { shrubs.contains(it) }
            pos2stateAllowed[p] = if (rng.isEmpty()) AllowedObjectState.Normal else AllowedObjectState.Error
        }
        // 3. The number on each Flowerbed tells you how many Shrubs are adjacent to it.
        for (area in flowerbeds) {
            val pHint = area.first { game.pos2hint[it] != null }
            val n1 = game.pos2hint[pHint]!!
            val shrubs2 = area
                .flatMap { p -> PondsAndFlowerbedsGame.offset.map { p + it } }
                .filter { shrubs.contains(it) }
                .toSet()
            val n2 = shrubs2.size
            val s = if (n1 == n2) HintState.Complete else HintState.Error
            pos2stateHint[pHint] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}