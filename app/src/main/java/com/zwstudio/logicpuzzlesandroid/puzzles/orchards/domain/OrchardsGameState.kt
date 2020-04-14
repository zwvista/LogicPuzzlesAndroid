package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import java.util.*

class OrchardsGameState(game: OrchardsGame?) : CellsGameState<OrchardsGame?, OrchardsGameMove?, OrchardsGameState?>(game) {
    var objArray: Array<OrchardsObject?>
    var pos2state: Map<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: OrchardsObject?) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: OrchardsObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: OrchardsGameMove): Boolean {
        if (!isValid(move.p) || get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: OrchardsGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<OrchardsObject, OrchardsObject> = label@ F<OrchardsObject, OrchardsObject> { obj: OrchardsObject? ->
            if (obj is OrchardsEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) OrchardsMarkerObject() else OrchardsTreeObject()
            if (obj is OrchardsTreeObject) return@label if (markerOption == MarkerOptions.MarkerLast) OrchardsMarkerObject() else OrchardsEmptyObject()
            if (obj is OrchardsMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) OrchardsTreeObject() else OrchardsEmptyObject()
            obj
        }
        val o: OrchardsObject? = get(move.p)
        move.obj = f.f(o)
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
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o: OrchardsObject? = get(p)
            if (o is OrchardsForbiddenObject) set(r, c, OrchardsEmptyObject()) else if (o is OrchardsTreeObject) {
                o.state = AllowedObjectState.Normal
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for (p in pos2node.keys) for (os in OrchardsGame.Companion.offset) {
            val p2 = p.add(os)
            if (pos2node.containsKey(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        while (!pos2node.isEmpty()) {
            g.setRootNode(fj.data.HashMap.fromMap(pos2node).values().head())
            val nodeList = g.bfs()
            val trees = fj.data.HashMap.fromMap(pos2node).toStream().filter(F<P2<Position, Node>, Boolean> { e: P2<Position?, Node?> -> nodeList.contains(e._2()) }).map<Position>(F<P2<Position, Node>, Position> { e: P2<Position?, Node?> -> e._1() }).toJavaList()
            // 2. These are Apple Trees, which must cross-pollinate, thus must be planted
            // in pairs - horizontally or vertically touching.
            if (trees.size != 2) isSolved = false
            // 3. A Tree must be touching just one other Tree: you can't put three or
            // more contiguous Trees.
            if (trees.size > 2) for (p in trees) {
                val o: OrchardsTreeObject? = get(p) as OrchardsTreeObject?
                o!!.state = AllowedObjectState.Error
            }
            for (p in trees) pos2node.remove(p)
        }
        for (a in game.areas) {
            val trees = mutableListOf<Position>()
            val n2 = 2
            for (p in a) if (get(p) is OrchardsTreeObject) trees.add(p)
            val n1 = trees.size
            // 4. At the same time, like in Parks, every country area must have exactly
            // two Trees in it.
            if (n1 != n2) isSolved = false
            for (p in a) {
                val o: OrchardsObject? = get(p)
                if (o is OrchardsTreeObject) {
                    val o2: OrchardsTreeObject = o
                    o2.state = if (o2.state == AllowedObjectState.Normal && n1 <= n2) AllowedObjectState.Normal else AllowedObjectState.Error
                } else if (o is OrchardsEmptyObject || o is OrchardsMarkerObject) {
                    if (n1 == n2 && allowedObjectsOnly) set(p, OrchardsForbiddenObject())
                }
            }
        }
    }

    init {
        objArray = arrayOfNulls<OrchardsObject>(rows() * cols())
        for (i in objArray.indices) objArray[i] = OrchardsEmptyObject()
    }
}