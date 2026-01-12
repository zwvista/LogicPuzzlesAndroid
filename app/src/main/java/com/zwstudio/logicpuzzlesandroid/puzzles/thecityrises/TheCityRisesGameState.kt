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
            is TheCityRisesEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) TheCityRisesMarkerObject else TheCityRisesTheCityRisesObject()
            is TheCityRisesTheCityRisesObject -> if (markerOption == MarkerOptions.MarkerLast) TheCityRisesMarkerObject else TheCityRisesEmptyObject
            is TheCityRisesMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) TheCityRisesTheCityRisesObject() else TheCityRisesEmptyObject
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
            for (c in 0 until cols)
                if (this[r, c] is TheCityRisesForbiddenObject)
                    this[r, c] = TheCityRisesEmptyObject
        // 2. A TheCityRises bar is a rectangular or a square.
        // 3. TheCityRises tiles form bars independently of the area borders.
        // 4. TheCityRises bars must not be orthogonally adjacent.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] !is TheCityRisesTheCityRisesObject) continue
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
            var r2 = 0
            var r1 = rows
            var c2 = 0
            var c1 = cols
            val bar = mutableListOf<Position>()
            for (node in nodeList) {
                val p = pos2node.filterValues { it == node }.keys.first()
                pos2node.remove(p)
                bar.add(p)
                if (r2 < p.row) r2 = p.row
                if (r1 > p.row) r1 = p.row
                if (c2 < p.col) c2 = p.col
                if (c1 > p.col) c1 = p.col
            }
            val rs = r2 - r1 + 1
            val cs = c2 - c1 + 1
            val s = if (rs * cs == nodeList.size) AllowedObjectState.Normal else AllowedObjectState.Error
            if (s != AllowedObjectState.Normal) isSolved = false
            for (p in bar)
                this[p] = TheCityRisesTheCityRisesObject(s)
        }
        // 5. A tile with a number indicates how many tiles in the area must
        //    be chocolate.
        // 6. An area without number can have any number of tiles of chocolate.
        for (area in game.areas) {
            val pHint = area.firstOrNull { game.pos2hint.contains(it) } ?: continue
            val n2 = game.pos2hint[pHint]!!
            val n1 = area.filter { this[it] is TheCityRisesTheCityRisesObject }.size
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            pos2state[pHint] = s
            if (!(allowedObjectsOnly && s != HintState.Normal)) continue
            val empties = area.filter { this[it] is TheCityRisesEmptyObject }
            for (p in empties)
                this[p] = TheCityRisesForbiddenObject
        }
    }
}