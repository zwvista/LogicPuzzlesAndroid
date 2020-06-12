package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class OrchardsGameState(game: OrchardsGame) : CellsGameState<OrchardsGame, OrchardsGameMove, OrchardsGameState>(game) {
    var objArray = Array<OrchardsObject>(rows() * cols()) { OrchardsEmptyObject() }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: OrchardsObject) {objArray[row * cols() + col] = dotObj}
    operator fun set(p: Position, obj: OrchardsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: OrchardsGameMove): Boolean {
        if (!isValid(move.p) || this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: OrchardsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is OrchardsEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) OrchardsMarkerObject() else OrchardsTreeObject()
            is OrchardsTreeObject -> if (markerOption == MarkerOptions.MarkerLast) OrchardsMarkerObject() else OrchardsEmptyObject()
            is OrchardsMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) OrchardsTreeObject() else OrchardsEmptyObject()
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
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val o: OrchardsObject? = get(p)
                if (o is OrchardsForbiddenObject)
                    this[r, c] = OrchardsEmptyObject()
                else if (o is OrchardsTreeObject) {
                    o.state = AllowedObjectState.Normal
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for (p in pos2node.keys)
            for (os in OrchardsGame.offset) {
                val p2 = p.add(os)
                if (pos2node.containsKey(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
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
                    (this[p] as OrchardsTreeObject).state = AllowedObjectState.Error
            for (p in trees)
                pos2node.remove(p)
        }
        for (a in game.areas) {
            val trees = mutableListOf<Position>()
            val n2 = 2
            for (p in a)
                if (this[p] is OrchardsTreeObject)
                    trees.add(p)
            val n1 = trees.size
            // 4. At the same time, like in Parks, every country area must have exactly
            // two Trees in it.
            if (n1 != n2) isSolved = false
            for (p in a) {
                val o = this[p]
                if (o is OrchardsTreeObject)
                    o.state = if (o.state == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                else if (o is OrchardsEmptyObject || o is OrchardsMarkerObject)
                    if (n1 == n2 && allowedObjectsOnly)
                        this[p] = OrchardsForbiddenObject()
            }
        }
    }
}