package com.zwstudio.logicpuzzlesandroid.puzzles.snakeislands

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.NurikabeGame

class SnakeIslandsGameState(game: SnakeIslandsGame) : CellsGameState<SnakeIslandsGame, SnakeIslandsGameMove, SnakeIslandsGameState>(game) {
    val objArray = Array<SnakeIslandsObject>(rows * cols) { SnakeIslandsObject.Empty }
    val pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: SnakeIslandsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: SnakeIslandsObject) {this[p.row, p.col] = obj}

    init {
        for ((p, obj) in game.pos2obj)
            this[p] = obj
        updateIsSolved()
    }

    override fun setObject(move: SnakeIslandsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p].isHint() || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: SnakeIslandsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p].isHint()) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            SnakeIslandsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) SnakeIslandsObject.Marker else SnakeIslandsObject.Wall
            SnakeIslandsObject.Wall -> if (markerOption == MarkerOptions.MarkerLast) SnakeIslandsObject.Marker else SnakeIslandsObject.Empty
            SnakeIslandsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) SnakeIslandsObject.Wall else SnakeIslandsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 4/Castle Patrol

        Summary
        Don't fall down the wall

        Description
        1. Divide the grid into walls and empty areas. Every area contains one number.
        2. The number indicates the size of the area. Numbers in wall tiles are part
           of wall areas; numbers in empty tiles are part of empty areas.
        3. Areas of the same type cannot share an edge.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        val rngEmpty = mutableListOf<Position>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                when (this[p]) {
                    SnakeIslandsObject.Empty, SnakeIslandsObject.EmptyHint ->
                        rngEmpty.add(p)
                    SnakeIslandsObject.Wall, SnakeIslandsObject.WallHint ->
                        rngWalls.add(p)
                    else -> {}
                }
            }
        for (p in rngWalls)
            for (os in NurikabeGame.offset) {
                val p2 = p + os
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        for (p in rngEmpty)
            for (os in NurikabeGame.offset) {
                val p2 = p + os
                if (rngEmpty.contains(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        val areas = mutableListOf<List<Position>>()
        val pos2area = mutableMapOf<Position, Int>()
        fun f(rngWE: MutableList<Position>, hint: SnakeIslandsObject) {
            while (rngWE.isNotEmpty()) {
                val node = pos2node[rngWE[0]]!!
                g.rootNode = node
                val nodeList = g.bfs()
                val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
                rngWE.removeAll { nodeList.contains(pos2node[it]) }
                val n1 = nodeList.size
                val rng = area.filter { game.pos2hint.containsKey(it) }
                if (rng.size == 1 && this[rng[0]] == hint) {
                    // 1. Divide the grid into walls and empty areas. Every area contains one number.
                    val p = rng[0]
                    val n2 = game.pos2hint[p]!!
                    val s = if (hint == SnakeIslandsObject.WallHint && n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
                    pos2state[p] = s
                    if (s != HintState.Complete) isSolved = false
                    val n = areas.size
                    areas.add(area)
                    for (p2 in area)
                        pos2area[p2] = n
                } else {
                    isSolved = false
                    for (p in rng) pos2state[p] = if (hint == SnakeIslandsObject.WallHint) HintState.Error else HintState.Normal
                }
            }
        }
        f(rngWalls, SnakeIslandsObject.WallHint)
        f(rngEmpty, SnakeIslandsObject.EmptyHint)
        if (!isSolved) return
        // 3. Areas of the same type cannot share an edge.
        if (!areas.all({ area ->
            val p0 = area[0]
            val n = pos2area[p0]!!
            val obj = this[p0]
            area.all({ p ->
                SnakeIslandsGame.offset.all({
                    val n2 = pos2area[p + it]
                    if (n2 == null || n2 == n)
                        true
                    else
                        this[areas[n2][0]] != obj
                })
            })
        })) isSolved = false
    }
}