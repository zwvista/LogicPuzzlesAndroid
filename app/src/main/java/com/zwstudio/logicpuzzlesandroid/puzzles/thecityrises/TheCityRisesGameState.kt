package com.zwstudio.logicpuzzlesandroid.puzzles.thecityrises

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.CloudsGame

class TheCityRisesGameState(game: TheCityRisesGame) : CellsGameState<TheCityRisesGame, TheCityRisesGameMove, TheCityRisesGameState>(game) {
    var objArray = Array<TheCityRisesObject>(rows * cols) { TheCityRisesEmptyObject }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: TheCityRisesObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: TheCityRisesObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: TheCityRisesGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: TheCityRisesGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is TheCityRisesEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) TheCityRisesMarkerObject else TheCityRisesBlockObject()
            is TheCityRisesBlockObject -> if (markerOption == MarkerOptions.MarkerLast) TheCityRisesMarkerObject else TheCityRisesEmptyObject
            is TheCityRisesMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) TheCityRisesBlockObject() else TheCityRisesEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 7/The City Rises

        Summary
        City Planner Revenge

        Description
        1. The board represents a piece of land where a new town should be built.
        2. Each area describes a section of land, where the town concil has decided
           to place as many city blocks as the number in it.
        3. Town blocks inside an area are horizontally or vertically contiguous.
        4. Blocks in different areas cannot touch horizontally or vertically.
        5. Areas without number can have any number of blocks, but there can't be
           empty areas.
        6. Lastly, two neighbouring areas can't have the same number of blocks in them.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                pos2state[p] = HintState.Normal
                if (this[p] is TheCityRisesForbiddenObject)
                    this[p] = TheCityRisesEmptyObject
            }
        // 3. Town blocks inside an area are horizontally or vertically contiguous.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] !is TheCityRisesBlockObject) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (p in pos2node.keys)
            for (os in CloudsGame.offset) {
                val p2 = p + os
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val blocks = mutableListOf<Position>()
            for (node in nodeList) {
                val p = pos2node.filterValues { it == node }.keys.first()
                pos2node.remove(p)
                blocks.add(p)
            }
            // 4. Blocks in different areas cannot touch horizontally or vertically.
            val cnt = blocks.map { game.pos2area[it]!! }.toSet().size
            val s = if (cnt == 1) AllowedObjectState.Normal else AllowedObjectState.Error
            if (s != AllowedObjectState.Normal) {
                isSolved = false
                for (p in blocks)
                    this[p] = TheCityRisesBlockObject(s)
            }
            if (s != AllowedObjectState.Normal) continue
            // 2. Each area describes a section of land, where the town concil has decided
            //    to place as many city blocks as the number in it.
            val nArea = game.pos2area[blocks[0]]!!
            val area = game.areas[nArea]
            val n1 = blocks.size
            // 5. Areas without number can have any number of blocks.
            val pHint = game.area2hint[nArea] ?: continue
            val n2 = game.pos2hint[pHint]!!
            val s2 = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (s2 != HintState.Complete) isSolved = false
            pos2state[pHint] = s2
            if (allowedObjectsOnly && s2 != HintState.Normal)
                area.filter { this[it] is TheCityRisesEmptyObject }.forEach {
                    this[it] = TheCityRisesForbiddenObject
                }
        }
        if (!isSolved) return
        val area2blocks = game.areas.map { it.filter { this[it] is TheCityRisesBlockObject }.size }
        // 5. There can't be empty areas.
        if (area2blocks.any { it == 0 }) isSolved = false
        // 6. Lastly, two neighbouring areas can't have the same number of blocks in them.
        for ((i, n) in area2blocks.withIndex()) {
            if (n == 0) continue
            val areas = game.area2areas[i].filter { area2blocks[it] == n }
            if (areas.isEmpty()) continue
            isSolved = false
            for (nArea in listOf(i) + areas) {
                val pHint = game.area2hint[nArea] ?: continue
                pos2state[pHint] = HintState.Error
            }
        }
    }
}