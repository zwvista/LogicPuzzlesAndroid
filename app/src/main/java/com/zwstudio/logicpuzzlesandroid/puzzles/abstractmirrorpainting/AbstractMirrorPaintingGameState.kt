package com.zwstudio.logicpuzzlesandroid.puzzles.abstractmirrorpainting

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class AbstractMirrorPaintingGameState(game: AbstractMirrorPaintingGame) : CellsGameState<AbstractMirrorPaintingGame, AbstractMirrorPaintingGameMove, AbstractMirrorPaintingGameState>(game) {
    var objArray = Array(rows * cols) { AbstractMirrorPaintingObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: AbstractMirrorPaintingObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: AbstractMirrorPaintingObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: AbstractMirrorPaintingGameMove): GameOperationType {
        val p = move.p
        val o = move.obj
        if (!isValid(p) || this[p] == o) return GameOperationType.Invalid
        this[p] = o
        for (p2 in game.areas[game.pos2area[p]!!])
            this[p2] = o
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: AbstractMirrorPaintingGameMove): GameOperationType {
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
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
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] == AbstractMirrorPaintingObject.Forbidden)
                    this[r, c] = AbstractMirrorPaintingObject.Empty
        // 2. A number indicates how many painted tiles are adjacent to it.
        for ((p, n2) in game.pos2hint) {
            val rng = mutableListOf<Position>()
            var n1 = 0
            for (os in AbstractMirrorPaintingGame.offset) {
                val p2 = p + os
                if (!isValid(p2)) continue
                val o = this[p2]
                if (o == AbstractMirrorPaintingObject.Painted)
                    n1++
                else if (o == AbstractMirrorPaintingObject.Empty)
                    rng.add(p2)
            }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete)
                isSolved = false
            else if (allowedObjectsOnly)
                for (p2 in rng)
                    this[p2] = AbstractMirrorPaintingObject.Forbidden
        }
        // 4. There can't be any 2*2 area of the same color(painted or empty).
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                if (AbstractMirrorPaintingGame.offset3.all { this[p + it] == AbstractMirrorPaintingObject.Painted } ||
                    AbstractMirrorPaintingGame.offset3.all { this[p + it] == AbstractMirrorPaintingObject.Empty }) {
                    isSolved = false
                    return
                }
            }
        if (!isSolved) return
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] == AbstractMirrorPaintingObject.Painted) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for (p in pos2node.keys)
            for (os in AbstractMirrorPaintingGame.offset) {
                val p2 = p + os
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        // 3. The painted tiles form an orthogonally continuous area, like a
        // Nurikabe.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}