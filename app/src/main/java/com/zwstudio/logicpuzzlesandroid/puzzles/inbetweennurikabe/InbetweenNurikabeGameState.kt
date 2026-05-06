package com.zwstudio.logicpuzzlesandroid.puzzles.inbetweennurikabe

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class InbetweenNurikabeGameState(game: InbetweenNurikabeGame) : CellsGameState<InbetweenNurikabeGame, InbetweenNurikabeGameMove, InbetweenNurikabeGameState>(game) {
    var objArray = Array(rows * cols) { InbetweenNurikabeObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()
    val invalid2x2Squares = mutableListOf<Position>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: InbetweenNurikabeObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: InbetweenNurikabeObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = InbetweenNurikabeObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: InbetweenNurikabeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == InbetweenNurikabeObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: InbetweenNurikabeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == InbetweenNurikabeObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            InbetweenNurikabeObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) InbetweenNurikabeObject.Marker else InbetweenNurikabeObject.Wall
            InbetweenNurikabeObject.Wall -> if (markerOption == MarkerOptions.MarkerLast) InbetweenNurikabeObject.Marker else InbetweenNurikabeObject.Empty
            InbetweenNurikabeObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) InbetweenNurikabeObject.Wall else InbetweenNurikabeObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 4/Inbetween Nurikabe

        Summary
        The garden between

        Description
        1. Create a Nurikabe, where each Garden has two numbers.
        2. The area of the garden must be between the two numbers.
        3. For example 2 and 4 give you an area of 3 tiles. 1 and 5 give you
           an area that can be 2, 3 or 4 tiles big.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 7. The wall can't form 2*2 squares.
        for (r in 0..<rows - 1)
            for (c in 0..<cols - 1) {
                val p = Position(r, c)
                val rng = InbetweenNurikabeGame.offset2.map { p + it }.filter { this[it] == InbetweenNurikabeObject.Wall }
                if (rng.size == 4) {
                    isSolved = false
                    invalid2x2Squares.add(p + Position.SouthEast)
                }
            }
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
                if (this[p] == InbetweenNurikabeObject.Wall)
                    rngWalls.add(p)
                else
                    rngEmpty.add(p)
            }
        for (p in rngWalls)
            for (os in InbetweenNurikabeGame.offset) {
                val p2 = p + os
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        for (p in rngEmpty)
            for (os in InbetweenNurikabeGame.offset) {
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
            if (rng.size == 2) {
                // 2. The area of the garden must be between the two numbers.
                val nums = rng.map { game.pos2hint[it]!! }.sorted()
                val s = if (nums[0] < n2 && n2 < nums[1]) HintState.Complete else HintState.Error
                for (p in rng) pos2state[p] = s
                if (s != HintState.Complete) isSolved = false
            } else {
                // 5. All the gardens in the puzzle are numbered at the start, there are no
                //    hidden gardens.
                isSolved = false
                for (p in rng) pos2state[p] = HintState.Normal
            }
        }
    }
}