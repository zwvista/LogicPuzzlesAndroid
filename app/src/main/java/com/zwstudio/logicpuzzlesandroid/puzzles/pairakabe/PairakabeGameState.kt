package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PairakabeGameState(game: PairakabeGame) : CellsGameState<PairakabeGame, PairakabeGameMove, PairakabeGameState>(game) {
    var objArray = Array(rows * cols) { PairakabeObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: PairakabeObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: PairakabeObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = PairakabeObject.Hint
    }

    override fun setObject(move: PairakabeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == PairakabeObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PairakabeGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == PairakabeObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            PairakabeObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) PairakabeObject.Marker else PairakabeObject.Wall
            PairakabeObject.Wall -> if (markerOption == MarkerOptions.MarkerLast) PairakabeObject.Marker else PairakabeObject.Empty
            PairakabeObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) PairakabeObject.Wall else PairakabeObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 12/Pairakabe

        Summary
        Just to confuse things a bit more

        Description
        1. Plays like Nurikabe, with an interesting twist.
        2. Instead of just one number, each 'garden' contains two numbers and
           the area of the garden is given by the sum of both.
    */
    private fun updateIsSolved() {
        isSolved = true
        // The wall can't form 2*2 squares.
        for (r in 0 until rows - 1)
            rule2x2@ for (c in 0 until cols - 1) {
                val p = Position(r, c)
                for (os in PairakabeGame.offset2)
                    if (this[p + os] != PairakabeObject.Wall)
                        continue@rule2x2
                isSolved = false
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
                if (this[p] == PairakabeObject.Wall)
                    rngWalls.add(p)
                else
                    rngEmpty.add(p)
            }
        for (p in rngWalls)
            for (os in PairakabeGame.offset) {
                val p2 = p + os
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        for (p in rngEmpty)
            for (os in PairakabeGame.offset) {
                val p2 = p + os
                if (rngEmpty.contains(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        if (rngWalls.isEmpty())
            isSolved = false
        else {
            // The garden is separated by a single continuous wall. This means all
            // wall tiles on the board must be connected horizontally or vertically.
            // There can't be isolated walls.
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
                0 ->                 // All the gardens in the puzzle are numbered at the start, there are no
                    // hidden gardens.
                    isSolved = false
                1 -> pos2state[rng[0]] = HintState.Error
                2 -> {
                    // 2. Instead of just one number, each 'garden' contains two numbers and
                    // the area of the garden is given by the sum of both.
                    val p1 = rng[0]
                    val p2 = rng[1]
                    val n1 = game.pos2hint[p1]!! + game.pos2hint[p2]!!
                    val s = if (n1 == n2) HintState.Complete else HintState.Error
                    pos2state[p1] = s
                    pos2state[p2] = s
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