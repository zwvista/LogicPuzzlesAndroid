package com.zwstudio.logicpuzzlesandroid.puzzles.pondcamping

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PondCampingGameState(game: PondCampingGame) : CellsGameState<PondCampingGame, PondCampingGameMove, PondCampingGameState>(game) {
    var objArray = Array(rows * cols) { PondCampingObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: PondCampingObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: PondCampingObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = PondCampingObject.Hint
    }

    override fun setObject(move: PondCampingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == PondCampingObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PondCampingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == PondCampingObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            PondCampingObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) PondCampingObject.Marker else PondCampingObject.Forest
            PondCampingObject.Forest -> if (markerOption == MarkerOptions.MarkerLast) PondCampingObject.Marker else PondCampingObject.Empty
            PondCampingObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) PondCampingObject.Forest else PondCampingObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 5/Pond camping

        Summary
        Splash!

        Description
        1. The numbers are Ponds. From each Pond you can have a hike of that many
           tiles as the number marked on it.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = get(p)
                if (!(o == PondCampingObject.Forest || o == PondCampingObject.Hint)) {
                    // 5. A camper can't cross forest or other Ponds.
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
            }
        for ((p, node) in pos2node) {
            for (os in PondCampingGame.offset) {
                val p2 = p + os
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
        for ((p, n2) in game.pos2hint) {
            val rng = mutableSetOf<Position>()
            for (os in PondCampingGame.offset) {
                val p2 = p + os
                val i = pos2area[p2] ?: continue
                rng.addAll(areas[i])
            }
            val n1 = rng.size
            // 1. The numbers are Ponds. From each Pond you can have a hike of that many
            //    tiles as the number marked on it.
            val s = if (n1 > n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
            if (allowedObjectsOnly && n1 <= n2)
                for (p2 in rng)
                    if (p2 != p)
                        this[p2] = PondCampingObject.Forbidden
        }
    }
}