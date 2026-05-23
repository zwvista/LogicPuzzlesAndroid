package com.zwstudio.logicpuzzlesandroid.puzzles.orchards

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class OrchardsGameState(game: OrchardsGame) : CellsGameState<OrchardsGame, OrchardsGameMove, OrchardsGameState>(game) {
    val objArray = Array(rows * cols) { OrchardsObject.Empty }
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: OrchardsObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: OrchardsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: OrchardsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: OrchardsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            OrchardsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) OrchardsObject.Marker else OrchardsObject.Tree
            OrchardsObject.Tree -> if (markerOption == MarkerOptions.MarkerLast) OrchardsObject.Marker else OrchardsObject.Empty
            OrchardsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) OrchardsObject.Tree else OrchardsObject.Empty
            else -> o

        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 11/Orchards

        Summary
        Plant the trees. Very close, this time!

        Description
        1. In a reverse of 'Parks', you're now planting Trees close together in
           neighboring country areas.
        2. These are Apple Trees, which must cross-pollinate, thus must be planted
           in pairs - horizontally or vertically touching.
        3. A Tree must be touching just one other Tree: you can't put three or
           more contiguous Trees.
        4. At the same time, like in Parks, every country area must have exactly
           two Trees in it.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o: OrchardsObject? = get(p)
                if (o == OrchardsObject.Forbidden)
                    this[r, c] = OrchardsObject.Empty
                else if (o == OrchardsObject.Tree) {
                    pos2state[p] = AllowedObjectState.Normal
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (os in OrchardsGame.offset) {
                val p2 = p + os
                pos2node[p2]?.let { g.connectNode(node, it) }
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val trees = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            // 2. These are Apple Trees, which must cross-pollinate, thus must be planted
            // in pairs - horizontally or vertically touching.
            if (trees.size != 2) isSolved = false
            // 3. A Tree must be touching just one other Tree: you can't put three or
            // more contiguous Trees.
            if (trees.size > 2)
                for (p in trees)
                    pos2state[p] = AllowedObjectState.Error
            for (p in trees)
                pos2node.remove(p)
        }
        for (a in game.areas) {
            val trees = mutableListOf<Position>()
            val n2 = 2
            for (p in a)
                if (this[p] == OrchardsObject.Tree)
                    trees.add(p)
            val n1 = trees.size
            // 4. At the same time, like in Parks, every country area must have exactly
            // two Trees in it.
            if (n1 != n2) isSolved = false
            for (p in a) {
                val o = this[p]
                if (o == OrchardsObject.Tree)
                    pos2state[p] = if (pos2state[p] == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if (o == OrchardsObject.Empty || o == OrchardsObject.Marker)
                    if (n1 == n2 && allowedObjectsOnly)
                        this[p] = OrchardsObject.Forbidden
            }
        }
    }
}