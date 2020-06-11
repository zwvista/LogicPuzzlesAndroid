package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGame

class ParkLakesGameState(game: ParkLakesGame) : CellsGameState<ParkLakesGame, ParkLakesGameMove, ParkLakesGameState>(game) {
    var objArray = Array<ParkLakesObject>(rows() * cols()) { ParkLakesEmptyObject() }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: ParkLakesObject) {objArray[row * cols() + col] = dotObj}
    operator fun set(p: Position, obj: ParkLakesObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = ParkLakesHintObject(tiles = n)
        updateIsSolved()
    }

    fun setObject(move: ParkLakesGameMove): Boolean {
        if (!isValid(move.p) || game.pos2hint[move.p] != null || this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: ParkLakesGameMove): Boolean {
        if (!isValid(move.p) || game.pos2hint[move.p] != null) return false
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is ParkLakesEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) ParkLakesMarkerObject() else ParkLakesTreeObject()
            is ParkLakesTreeObject -> if (markerOption == MarkerOptions.MarkerLast) ParkLakesMarkerObject() else ParkLakesEmptyObject()
            is ParkLakesMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) ParkLakesTreeObject() else ParkLakesEmptyObject()
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 15/Park Lakes

        Summary
        Find the Lakes

        Description
        1. The board represents a park, where there are some hidden lakes, all square
           in shape.
        2. You have to find the lakes with the aid of hints, knowing that:
        3. A number tells you the total size of the any lakes orthogonally touching it,
           while a question mark tells you that there is at least one lake orthogonally
           touching it.
        4. Lakes aren't on tiles with numbers or question marks.
        5. All the land tiles are connected horizontally or vertically.
    */
    private fun updateIsSolved() {
        isSolved = true
        var g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val o = this[p]
                if (o is ParkLakesTreeObject) {
                    o.state = AllowedObjectState.Normal
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                } else if (o is ParkLakesHintObject)
                    o.state = HintState.Normal
            }
        for ((p, node) in pos2node) {
            for (os in TierraDelFuegoGame.offset) {
                val p2 = p.add(os)
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
            var r2 = 0
            var r1 = rows()
            var c2 = 0
            var c1 = cols()
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
            if (!(rs == cs && rs * cs == nodeList.size)) {
                isSolved = false
                for (p in area)
                    (this[p] as ParkLakesTreeObject).state = AllowedObjectState.Error
            }
        }
        for ((p, n2) in game.pos2hint.entries) {
            var n1 = 0
            for (os in ParkLakesGame.offset) {
                val i = pos2area[p.add(os)] ?: continue
                n1 += areas[i].size
            }
            // 3. A number tells you the total size of any lakes orthogonally touching it,
            // while a question mark tells you that there is at least one lake orthogonally
            // touching it.
            val s = if (n1 == 0) HintState.Normal else if (n1 == n2 || n2 == -1) HintState.Complete else HintState.Error
            (this[p] as ParkLakesHintObject).state = s
            if (s != HintState.Complete) isSolved = false
        }
        g = Graph()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                if (this[p] !is ParkLakesTreeObject) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node)
            for (os in TierraDelFuegoGame.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2] ?: continue
                g.connectNode(node, node2)
            }
        // 5. All the land tiles are connected horizontally or vertically.
        g.rootNode = pos2node.values.first()
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }
}