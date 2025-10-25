package com.zwstudio.logicpuzzlesandroid.puzzles.archipelago

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.TierraDelFuegoGame

class ArchipelagoGameState(game: ArchipelagoGame) : CellsGameState<ArchipelagoGame, ArchipelagoGameMove, ArchipelagoGameState>(game) {
    var objArray = Array<ArchipelagoObject>(rows * cols) { ArchipelagoEmptyObject }
    val invalid2x2Squares = mutableListOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: ArchipelagoObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: ArchipelagoObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = ArchipelagoHintObject()
        updateIsSolved()
    }

    override fun setObject(move: ArchipelagoGameMove): GameOperationType {
        if (!isValid(move.p) || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ArchipelagoGameMove): GameOperationType {
        if (!isValid(move.p) || game.pos2hint[move.p] != null) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is ArchipelagoEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) ArchipelagoMarkerObject else ArchipelagoWaterObject()
            is ArchipelagoWaterObject -> if (markerOption == MarkerOptions.MarkerLast) ArchipelagoMarkerObject else ArchipelagoEmptyObject
            is ArchipelagoMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) ArchipelagoWaterObject() else ArchipelagoEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 8/Archipelago

        Summary
        No bridges, just find the water

        Description
        1. Each number represents a rectangular island in the Archipelago.
        2. The number in itself identifies how many squares the island occupies.
        3. Islands can only touch each other diagonally and by touching they
           must form a network where no island is isolated from the others.
        4. In other words, every island must be touching another island diagonally
           and no group of islands must be separated from the others.
        5. Not all the islands you need to find are necessarily marked by numbers.
        6. Finally, no 2*2 square can be occupied by water only (just like Nurikabe).
    */
    private fun updateIsSolved() {
        isSolved = true
        // 6. Finally, no 2*2 square can be occupied by water only (just like Nurikabe).
        invalid2x2Squares.clear()
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val isFullOfWater = ArchipelagoGame.offset2.map { p + it }.all { this[it] is ArchipelagoWaterObject }
                if (isFullOfWater) { invalid2x2Squares.add(p + Position.SouthEast); isSolved = false }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is ArchipelagoWaterObject) continue
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for ((p, node) in pos2node) {
            for (os in TierraDelFuegoGame.offset) {
                val p2 = p + os
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
            }
        }
        val areas = mutableListOf<List<Position>>()
        val pos2area = mutableMapOf<Position, Int>()
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            val n1 = nodeList.size
            var r2 = 0
            var r1 = rows
            var c2 = 0
            var c1 = cols
            val n = areas.size
            for (node in nodeList) {
                val p = pos2node.filter { it.value == node }.keys.first()
                pos2node.remove(p)
                if (r2 < p.row) r2 = p.row
                if (r1 > p.row) r1 = p.row
                if (c2 < p.col) c2 = p.col
                if (c1 > p.col) c1 = p.col
                pos2area[p] = n
            }
            areas.add(area)
            val rs = r2 - r1 + 1
            val cs = c2 - c1 + 1
            // 1. Each number represents a rectangular island in the Archipelago.
            val isRect = rs * cs == n1
            val hints = area.filter { this[it] is ArchipelagoHintObject }
            if (hints.size > 1) {
                isSolved = false
                for (p in hints) { this[p] = ArchipelagoHintObject() }
            } else if (hints.size == 1) {
                val p = hints[0]
                val n2 = game.pos2hint[p]!!
                // 2. The number in itself identifies how many squares the island occupies.
                val s = if (!isRect || n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
                this[p] = ArchipelagoHintObject(state = s)
                if (s != HintState.Complete) isSolved = false
            }
        }
        val g2 = Graph()
        for (i in areas.indices) {
            val node = Node("$i")
            g2.addNode(node)
        }
        for ((i, area) in areas.withIndex()) {
            val indexes = mutableSetOf<Int>()
            for (p in area)
                for (os in ArchipelagoGame.offset3) {
                    val j = pos2area[p + os]
                    if (j != null && j != i)
                        indexes.add(j)
                }
            if (indexes.isEmpty()) { isSolved = false; return }
            for (j in indexes)
                g2.connectNode(g2.nodes[i], g2.nodes[j])
        }
        g2.rootNode = g2.nodes[0]
        val nodeList = g2.bfs()
        if (nodeList.size != g2.nodes.size) isSolved = false
    }
}