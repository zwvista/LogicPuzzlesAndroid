package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class CarpentersSquareGameState(game: CarpentersSquareGame) : CellsGameState<CarpentersSquareGame, CarpentersSquareGameMove, CarpentersSquareGameState>(game) {
    var objArray: Array<Array<GridLineObject>> = Cloner().deepClone(game.objArray)
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]

    init {
        updateIsSolved()
    }

    fun setObject(move: CarpentersSquareGameMove): Boolean {
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1 + CarpentersSquareGame.offset[dir]
        if (game[p1][dir] != GridLineObject.Empty || !isValid(p2)) return false
        val o = this[p1][dir]
        if (o == move.obj) return false
        this[p1][dir] = move.obj
        this[p2][dir2] = this[p1][dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: CarpentersSquareGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p][move.dir]
        move.obj = when (o) {
            GridLineObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
            GridLineObject.Line -> if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
            GridLineObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 16/Carpenter's Square

        Summary
        Angled Borders

        Description
        1. Similar to Carpenter's Wall, this time you have to respect the same
           rules, but instead of forming a Nurikabe, you just have to divide the
           board into many.Capenter's Squares (L shaped tools) of different size.
        2. The circled numbers on the board indicate the corner of the L.
        3. When a number is inside the circle, that indicates the total number of
           squares occupied by the L.
        4. The arrow always sits at the end of an arm and points to the corner of
           an L.
        5. All the tiles in the board have to be part of a Carpenter's Square.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        for (r in 0 until rows - 1)
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                for (i in 0 until 4)
                    if (this[p + CarpentersSquareGame.offset2[i]][CarpentersSquareGame.dirs[i]] != GridLineObject.Line)
                        g.connectNode(pos2node[p]!!, pos2node[p + CarpentersSquareGame.offset[i]]!!)
            }
        while (pos2node.isNotEmpty()) {
            g.rootNode = pos2node.values.first()
            val nodeList = g.bfs()
            val area = pos2node.filter { nodeList.contains(it.value) }.map { it.key }
            for (p in area)
                pos2node.remove(p)
            val rngHint = area.filter { game.pos2hint.containsKey(it) }
            val n1 = nodeList.size
            var r2 = 0
            var r1 = rows
            var c2 = 0
            var c1 = cols
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
            // 1. You just have to divide the board into many.Capenter's Squares (L shaped tools) of different size.
            val squareType = when {
                f(cntR1, cntC1) -> 0  // ┌
                f(cntR1, cntC2) -> 1  // ┐
                f(cntR2, cntC1) -> 2  // └
                f(cntR2, cntC2) -> 3  // ┘
                else -> -1
            }
            // 5. All the tiles in the board have to be part of a Carpenter's Square.
            if (squareType == -1) isSolved = false
            for (p in rngHint)
                when (val o = game.pos2hint[p]) {
                    is CarpentersSquareCornerHint -> {
                        // 2. The circled numbers on the board indicate the corner of the L.
                        // 3. When a number is inside the circle, that indicates the total number of
                        // squares occupied by the L.
                        val n2 = o.tiles
                        val s = if (squareType == -1) HintState.Normal else if (!(n1 == n2 || n2 == 0)) HintState.Error else if (squareType == 0 && p == Position(r1, c1) ||
                                squareType == 1 && p == Position(r1, c2) || squareType == 2 && p == Position(r2, c1) || squareType == 3 && p == Position(r2, c2)) HintState.Complete else HintState.Error
                        pos2state[p] = s
                        if (s != HintState.Complete) isSolved = false
                    }
                    is CarpentersSquareLeftHint -> {
                        // 4. The arrow always sits at the end of an arm and points to the corner of
                        // an L.
                        val s = if (squareType == -1) HintState.Normal else if (squareType == 0 && p == Position(r1, c2) ||
                                squareType == 2 && p == Position(r2, c2)) HintState.Complete else HintState.Error
                        pos2state[p] = s
                        if (s != HintState.Complete) isSolved = false
                    }
                    is CarpentersSquareUpHint -> {
                        // 4. The arrow always sits at the end of an arm and points to the corner of
                        // an L.
                        val s = if (squareType == -1) HintState.Normal else if (squareType == 0 && p == Position(r2, c1) ||
                                squareType == 1 && p == Position(r2, c2)) HintState.Complete else HintState.Error
                        pos2state[p] = s
                        if (s != HintState.Complete) isSolved = false
                    }
                    is CarpentersSquareRightHint -> {
                        // 4. The arrow always sits at the end of an arm and points to the corner of
                        // an L.
                        val s = if (squareType == -1) HintState.Normal else if (squareType == 1 && p == Position(r1, c1) ||
                                squareType == 3 && p == Position(r2, c1)) HintState.Complete else HintState.Error
                        pos2state[p] = s
                        if (s != HintState.Complete) isSolved = false
                    }
                    is CarpentersSquareDownHint -> {
                        // 4. The arrow always sits at the end of an arm and points to the corner of
                        // an L.
                        val s = if (squareType == -1) HintState.Normal else if (squareType == 2 && p == Position(r1, c1) ||
                                squareType == 3 && p == Position(r1, c2)) HintState.Complete else HintState.Error
                        pos2state[p] = s
                        if (s != HintState.Complete) isSolved = false
                    }
                }
        }
    }
}