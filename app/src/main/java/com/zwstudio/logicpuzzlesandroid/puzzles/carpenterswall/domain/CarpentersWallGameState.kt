package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class CarpentersWallGameState(game: CarpentersWallGame) : CellsGameState<CarpentersWallGame, CarpentersWallGameMove, CarpentersWallGameState>(game) {
    var objArray = game.objArray.copyOf()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: CarpentersWallObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: CarpentersWallObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: CarpentersWallGameMove): Boolean {
        val p = move.p
        val objOld = this[p]
        val objNew = move.obj
        if (objOld.isHint || objOld == objNew) return false
        this[p] = objNew
        updateIsSolved()
        return true
    }

    fun switchObject(move: CarpentersWallGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        if (o.isHint) return false
        move.obj = when(o) {
            is CarpentersWallEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) CarpentersWallMarkerObject() else CarpentersWallWallObject()
            is CarpentersWallWallObject -> if (markerOption == MarkerOptions.MarkerLast) CarpentersWallMarkerObject() else CarpentersWallEmptyObject()
            is CarpentersWallMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) CarpentersWallWallObject() else CarpentersWallEmptyObject()
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 12/Carpenter's Wall

        Summary
        Angled Walls

        Description
        1. In this game you have to create a valid Nurikabe following a different
           type of hints.
        2. In the end, the empty spaces left by the Nurikabe will form many Carpenter's
           Squares (L shaped tools) of different size.
        3. The circled numbers on the board indicate the corner of the L.
        4. When a number is inside the circle, that indicates the total number of
           squares occupied by the L.
        5. The arrow always sits at the end of an arm and points to the corner of
           an L.
        6. Not all the Carpenter's Squares might be indicated: some could be hidden
           and no hint given.
    */
    private fun updateIsSolved() {
        isSolved = true
        // The wall can't form 2*2 squares.
        for (r in 0 until rows() - 1)
            rule2x2@ for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                for (os in CarpentersWallGame.offset2)
                    if (this[p.add(os)] !is CarpentersWallWallObject) continue@rule2x2
                isSolved = false
            }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls = mutableListOf<Position>()
        val rngEmpty = mutableListOf<Position>()
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
                val o = this[p]
                if (o is CarpentersWallWallObject)
                    rngWalls.add(p)
                else
                    rngEmpty.add(p)
                if (o is CarpentersWallCornerObject)
                    o.state = HintState.Normal
                else if (o is CarpentersWallLeftObject)
                    o.state = HintState.Normal
                else if (o is CarpentersWallRightObject)
                    o.state = HintState.Normal
                else if (o is CarpentersWallUpObject)
                    o.state = HintState.Normal
                else if (o is CarpentersWallDownObject)
                    o.state = HintState.Normal
            }
        for (p in rngWalls)
            for (os in CarpentersWallGame.offset) {
                val p2 = p.add(os)
                if (rngWalls.contains(p2))
                    g.connectNode(pos2node[p]!!, pos2node[p2]!!)
            }
        for (p in rngEmpty)
            for (os in CarpentersWallGame.offset) {
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
            val node = pos2node[rngEmpty[0]]!!
            g.rootNode = node
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            rngEmpty.removeAll { nodeList.contains(pos2node[it]) }
            val rngHint = area.filter { this[it].isHint }
            val n1 = nodeList.size
            var r2 = 0
            var r1 = rows()
            var c2 = 0
            var c1 = cols()
            for (p in area) {
                if (r2 < p.row) r2 = p.row
                if (r1 > p.row) r1 = p.row
                if (c2 < p.col) c2 = p.col
                if (c1 > p.col) c1 = p.col
            }
            if (r1 == r2 || c1 == c2) {
                isSolved = false
                continue
            }
            val cntR1 = area.filter { p: Position -> p.row == r1 }.size
            val cntR2 = area.filter { p: Position -> p.row == r2 }.size
            val cntC1 = area.filter { p: Position -> p.col == c1 }.size
            val cntC2 = area.filter { p: Position -> p.col == c2 }.size
            fun f(a: Int, b: Int) = a > 1 && b > 1 && a + b - 1 == n1
            // 2. In the end, the empty spaces left by the Nurikabe will form many Carpenter's
            // Squares (L shaped tools) of different size.
            val squareType = when {
                f(cntR1, cntC1) -> 0   // ┌
                f(cntR1, cntC2) -> 1   // ┐
                f(cntR2, cntC1) -> 2   // └
                f(cntR2, cntC2) -> 3   // ┘
                else -> -1
            }
            for (p in rngHint)
                when (val o = this[p]) {
                    is CarpentersWallCornerObject -> {
                        // 3. The circled numbers on the board indicate the corner of the L.
                        // 4. When a number is inside the circle, that indicates the total number of
                        // squares occupied by the L.
                        val n2 = o.tiles
                        val s = if (squareType == -1) HintState.Normal else if (!(n1 == n2 || n2 == 0)) HintState.Error else if (squareType == 0 && p == Position(r1, c1) ||
                                squareType == 1 && p == Position(r1, c2) || squareType == 2 && p == Position(r2, c1) || squareType == 3 && p == Position(r2, c2)) HintState.Complete else HintState.Error
                        o.state = s
                        if (s != HintState.Complete) isSolved = false
                    }
                    is CarpentersWallLeftObject -> {
                        // 5. The arrow always sits at the end of an arm and points to the corner of
                        // an L.
                        val s = if (squareType == -1) HintState.Normal else if (squareType == 0 && p == Position(r1, c2) ||
                                squareType == 2 && p == Position(r2, c2)) HintState.Complete else HintState.Error
                        o.state = s
                        if (s != HintState.Complete) isSolved = false
                    }
                    is CarpentersWallUpObject -> {
                        // 5. The arrow always sits at the end of an arm and points to the corner of
                        // an L.
                        val s = if (squareType == -1) HintState.Normal else if (squareType == 0 && p == Position(r2, c1) ||
                                squareType == 1 && p == Position(r2, c2)) HintState.Complete else HintState.Error
                        o.state = s
                        if (s != HintState.Complete) isSolved = false
                    }
                    is CarpentersWallRightObject -> {
                        // 5. The arrow always sits at the end of an arm and points to the corner of
                        // an L.
                        val s = if (squareType == -1) HintState.Normal else if (squareType == 1 && p == Position(r1, c1) ||
                                squareType == 3 && p == Position(r2, c1)) HintState.Complete else HintState.Error
                        o.state = s
                        if (s != HintState.Complete) isSolved = false
                    }
                    is CarpentersWallDownObject -> {
                        // 5. The arrow always sits at the end of an arm and points to the corner of
                        // an L.
                        val s = if (squareType == -1) HintState.Normal else if (squareType == 2 && p == Position(r1, c1) ||
                                squareType == 3 && p == Position(r1, c2)) HintState.Complete else HintState.Error
                        o.state = s
                        if (s != HintState.Complete) isSolved = false
                    }
                }
        }
    }
}