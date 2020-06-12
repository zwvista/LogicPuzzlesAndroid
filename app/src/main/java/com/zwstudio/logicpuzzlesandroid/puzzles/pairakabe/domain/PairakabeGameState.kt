package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class PairakabeGameState(game: PairakabeGame) : CellsGameState<PairakabeGame, PairakabeGameMove, PairakabeGameState>(game) {
    var objArray = Array<PairakabeObject>(rows() * cols()) { PairakabeEmptyObject() }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: PairakabeObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: PairakabeObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = PairakabeHintObject()
    }

    fun setObject(move: PairakabeGameMove): Boolean {
        val p = move.p
        val objOld = this[p]
        val objNew = move.obj
        if (objOld is PairakabeHintObject || objOld.toString() == objNew.toString()) return false
        this[p] = objNew
        updateIsSolved()
        return true
    }

    fun switchObject(move: PairakabeGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is PairakabeEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) PairakabeMarkerObject() else PairakabeWallObject()
            is PairakabeWallObject -> if (markerOption == MarkerOptions.MarkerLast) PairakabeMarkerObject() else PairakabeEmptyObject()
            is PairakabeMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) PairakabeWallObject() else PairakabeEmptyObject()
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
        for (r in 0 until rows() - 1)
            rule2x2@ for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                for (os in PairakabeGame.offset2)
                    if (this[p.add(os)] !is PairakabeWallObject)
                        continue@rule2x2
                isSolved = false
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        var rngEmpty = mutableListOf<Position>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                if (get(p) is PairakabeWallObject)
                    rngWalls.add(p)
                else
                    rngEmpty.add(p)
            }
        for (p in rngWalls)
            for (os in PairakabeGame.offset) {
                val p2 = p.add(os)
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        for (p in rngEmpty)
            for (os in PairakabeGame.offset) {
                val p2 = p.add(os)
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
            val node = pos2node[rngEmpty[0]]
            g.rootNode = node
            val nodeList = g.bfs()
            rngEmpty.removeAll { nodeList.contains(pos2node[it]) }
            val n2 = nodeList.size
            val rng = mutableListOf<Position>()
            for (p in game.pos2hint.keys)
                if (nodeList.contains(pos2node[p]))
                    rng.add(p.plus())
            when (rng.size) {
                0 ->                 // All the gardens in the puzzle are numbered at the start, there are no
                    // hidden gardens.
                    isSolved = false
                1 -> (this[rng[0]] as PairakabeHintObject).state = HintState.Error
                2 -> {
                    // 2. Instead of just one number, each 'garden' contains two numbers and
                    // the area of the garden is given by the sum of both.
                    val p1 = rng[0]
                    val p2 = rng[1]
                    val n1 = game.pos2hint[p1]!! + game.pos2hint[p2]!!
                    val s = if (n1 == n2) HintState.Complete else HintState.Error
                    (this[p1] as PairakabeHintObject).state = s
                    (this[p2] as PairakabeHintObject).state = s
                    if (s != HintState.Complete) isSolved = false
                }
                else -> {
                    for (p in rng)
                        (this[p] as PairakabeHintObject).state = HintState.Normal
                    isSolved = false
                }
            }
        }
    }
}