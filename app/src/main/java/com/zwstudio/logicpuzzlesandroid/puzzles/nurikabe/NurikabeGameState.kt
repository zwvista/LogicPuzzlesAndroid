package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NurikabeGameState(game: NurikabeGame) : CellsGameState<NurikabeGame, NurikabeGameMove, NurikabeGameState>(game) {
    var objArray = Array(rows * cols) { NurikabeObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()
    val invalid2x2Squares = mutableListOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: NurikabeObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: NurikabeObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = NurikabeObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: NurikabeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == NurikabeObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: NurikabeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == NurikabeObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            NurikabeObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) NurikabeObject.Marker else NurikabeObject.Wall
            NurikabeObject.Wall -> if (markerOption == MarkerOptions.MarkerLast) NurikabeObject.Marker else NurikabeObject.Empty
            NurikabeObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) NurikabeObject.Wall else NurikabeObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/Nurikabe

        Summary
        Draw a continuous wall that divides gardens as big as the numbers

        Description
        1. Each number on the grid indicates a garden, occupying as many tiles
           as the number itself.
        2. Gardens can have any form, extending horizontally and vertically, but
           can't extend diagonally.
        3. The garden is separated by a single continuous wall. This means all
           wall tiles on the board must be connected horizontally or vertically.
           There can't be isolated walls.
        4. You must find and mark the wall following these rules:
        5. All the gardens in the puzzle are numbered at the start, there are no
           hidden gardens.
        6. A wall can't go over numbered squares.
        7. The wall can't form 2*2 squares.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 7. The wall can't form 2*2 squares.
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val rng = NurikabeGame.offset2.map { p + it }.filter { this[it] == NurikabeObject.Wall }
                if (rng.size == 4) {
                    isSolved = false
                    invalid2x2Squares.add(p + Position.SouthEast)
                }
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        val rngEmpty = mutableListOf<Position>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (this[p] == NurikabeObject.Wall)
                    rngWalls.add(p)
                else
                    rngEmpty.add(p)
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
        if (rngWalls.isEmpty())
            isSolved = false
        else {
            // 3. The garden is separated by a single continuous wall. This means all
            //    wall tiles on the board must be connected horizontally or vertically.
            //    There can't be isolated walls.
            g.rootNode = pos2node[rngWalls[0]]!!
            val nodeList = g.bfs()
            if (rngWalls.size != nodeList.size) isSolved = false
        }
        while (rngEmpty.isNotEmpty()) {
            val node = pos2node[rngEmpty[0]]!!
            g.rootNode = node
            val nodeList = g.bfs()
            rngEmpty.removeAll { nodeList.contains(pos2node[it]) }
            val n2 = nodeList.size
            val rng = mutableListOf<Position>()
            for (p in game.pos2hint.keys)
                if (nodeList.contains(pos2node[p]))
                    rng.add(+p)
            when (rng.size) {
                0 ->
                    // 5. All the gardens in the puzzle are numbered at the start, there are no
                    //    hidden gardens.
                    isSolved = false
                1 -> {
                    // 1. Each number on the grid indicates a garden, occupying as many tiles
                    //    as the number itself.
                    val p = rng[0]
                    val n1 = game.pos2hint[p]!!
                    val s = if (n1 == n2) HintState.Complete else HintState.Error
                    pos2state[p] = s
                    if (s != HintState.Complete) isSolved = false
                }
                else -> {
                    for (p in rng)
                        pos2state[p] = HintState.Normal
                    isSolved = false
                }
            }
        }
    }
}