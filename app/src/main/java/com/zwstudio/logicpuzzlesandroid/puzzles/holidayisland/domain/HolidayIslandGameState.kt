package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class HolidayIslandGameState(game: HolidayIslandGame) : CellsGameState<HolidayIslandGame, HolidayIslandGameMove, HolidayIslandGameState>(game) {
    var objArray = Array<HolidayIslandObject>(rows * cols) {  HolidayIslandEmptyObject() }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: HolidayIslandObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: HolidayIslandObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = HolidayIslandHintObject(tiles = n)
    }

    fun setObject(move: HolidayIslandGameMove): Boolean {
        if (!isValid(move.p) || game.pos2hint[move.p] != null || this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: HolidayIslandGameMove): Boolean {
        if (!isValid(move.p) || game.pos2hint[move.p] != null) return false
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when(o) {
            is HolidayIslandEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) HolidayIslandMarkerObject() else HolidayIslandTreeObject()
            is HolidayIslandTreeObject -> if (markerOption == MarkerOptions.MarkerLast) HolidayIslandMarkerObject() else HolidayIslandEmptyObject()
            is HolidayIslandMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) HolidayIslandTreeObject() else HolidayIslandEmptyObject()
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 11/Holiday Island

        Summary
        This time the campers won't have their way!

        Description
        1. This time the resort is an island, the place is packed and the campers
           (Tents) must compromise!
        2. The board represents an Island, where there are a few Tents, identified
           by the numbers.
        3. Your job is to find the water surrounding the island, with these rules:
        4. There is only one, continuous island.
        5. The numbers tell you how many tiles that camper can walk from his Tent,
           by moving horizontally or vertically. A camper can't cross water or
           other Tents.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val rngHints = mutableListOf<Position>()
        var g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is HolidayIslandForbiddenObject)
                    this[p] = HolidayIslandEmptyObject()
                else if (o is HolidayIslandHintObject) {
                    o.state = HintState.Normal
                    rngHints.add(p)
                }
                if (o !is HolidayIslandTreeObject) {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (os in HolidayIslandGame.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null)
                    g.connectNode(node, node2)
            }
        }
        run {

            // 4. There is only one, continuous island.
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            if (nodeList.size != pos2node.size) isSolved = false
        }
        g = Graph()
        pos2node.clear()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = get(p)
                if (!(o is HolidayIslandTreeObject || o is HolidayIslandHintObject)) {
                    // 5. A camper can't cross water or other Tents.
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (os in HolidayIslandGame.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2] ?: continue
                g.connectNode(node, node2)
            }
        }
        val areas = mutableListOf<List<Position>>()
        val pos2area = mutableMapOf<Position, Int>()
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            val n = areas.size
            for (node in nodeList) {
                val p = pos2node.filter { it.value == node }.keys.first()
                pos2node.remove(p)
                pos2area[p] = n
            }
            areas.add(area)
        }
        for (p in rngHints) {
            val n2 = game.pos2hint[p]!!
            val rng = mutableSetOf<Position>()
            for (os in HolidayIslandGame.offset) {
                val p2 = p.add(os)
                val i = pos2area[p2] ?: continue
                rng.addAll(areas[i])
            }
            val n1 = rng.size
            // 5. The numbers tell you how many tiles that camper can walk from his Tent,
            // by moving horizontally or vertically.
            val s = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            (this[p] as HolidayIslandHintObject).state = s
            if (s != HintState.Complete) isSolved = false
            if (allowedObjectsOnly && n1 <= n2)
                for (p2 in rng)
                    if (p2 != p)
                        this[p2] = HolidayIslandForbiddenObject()
        }
    }
}