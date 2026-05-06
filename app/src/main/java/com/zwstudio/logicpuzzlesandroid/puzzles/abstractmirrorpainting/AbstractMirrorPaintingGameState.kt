package com.zwstudio.logicpuzzlesandroid.puzzles.abstractmirrorpainting

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class AbstractMirrorPaintingGameState(game: AbstractMirrorPaintingGame) : CellsGameState<AbstractMirrorPaintingGame, AbstractMirrorPaintingGameMove, AbstractMirrorPaintingGameState>(game) {
    var objArray = Array(rows * cols) { AbstractMirrorPaintingObject.Empty }
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: AbstractMirrorPaintingObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: AbstractMirrorPaintingObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: AbstractMirrorPaintingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: AbstractMirrorPaintingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            AbstractMirrorPaintingObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) AbstractMirrorPaintingObject.Marker else AbstractMirrorPaintingObject.Painted
            AbstractMirrorPaintingObject.Painted -> if (markerOption == MarkerOptions.MarkerLast) AbstractMirrorPaintingObject.Marker else AbstractMirrorPaintingObject.Empty
            AbstractMirrorPaintingObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) AbstractMirrorPaintingObject.Painted else AbstractMirrorPaintingObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 4/Abstract Mirror Painting

        Summary
        Aliens, move over, the Next Trend is here!

        Description
        1. Diagonal mirrors are out, the new trend is orthogonal mirror abstract painting!
        2. You should paint areas that span two adjacent regions. The area is symmetrical with respect
           to the regions border.
        3. Numbers tell you how many tiles in that region are painted.
        4. Areas can't touch orthogonally.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                pos2stateAllowed[p] = AllowedObjectState.Normal
                if (this[p] == AbstractMirrorPaintingObject.Forbidden)
                    this[p] = AbstractMirrorPaintingObject.Empty
            }
        // 3. Numbers tell you how many tiles in that region are painted.
        for ((p, n2) in game.pos2hint) {
            val area = game.areas[game.pos2area[p]!!]
            val n1 = area.count { this[it] == AbstractMirrorPaintingObject.Painted }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2stateHint[p] = s
            if (s != HintState.Complete)
                isSolved = false
            if (allowedObjectsOnly && s != HintState.Normal)
                for (p2 in area)
                    if (this[p2] == AbstractMirrorPaintingObject.Empty || this[p2] == AbstractMirrorPaintingObject.Marker)
                        this[p2] = AbstractMirrorPaintingObject.Forbidden
        }
        // 2. A number indicates how many painted tiles are adjacent to it.
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p] == AbstractMirrorPaintingObject.Painted) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (os in AbstractMirrorPaintingGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val painting = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in painting)
                pos2node.remove(p)
            val areaSet = painting.map { game.pos2area[it]!! }.toSortedSet()
            if (areaSet.size != 2)
                for (p in painting)
                    pos2stateAllowed[p] = AllowedObjectState.Error
            else {
                val (areaId1, areaId2) = areaSet.first() to areaSet.last()
                val painting1 = painting.filter { game.pos2area[it] == areaId1 }
                val painting2 = painting.filter { game.pos2area[it] == areaId2 }
                val mirrors = game.mirrors.filter { it.areaId1 == areaId1 && it.areaId2 == areaId2 }
                if (!mirrors.any {
                    val (p1, p2) = it.p1 to it.p2
                    painting1.all {
                        painting2.contains(it - p1 + p2)
                    }
                }) {
                    isSolved = false
                    for (p in painting)
                        pos2stateAllowed[p] = AllowedObjectState.Error
                }
            }
        }
    }
}