package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.F2
import fj.P2
import fj.data.List
import java.util.*

class CarpentersWallGameState(game: CarpentersWallGame) : CellsGameState<CarpentersWallGame?, CarpentersWallGameMove?, CarpentersWallGameState?>(game) {
    var objArray: Array<CarpentersWallObject?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position?) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: CarpentersWallObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position?, obj: CarpentersWallObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: CarpentersWallGameMove?): Boolean {
        val p = move!!.p
        val objOld = get(p)
        val objNew = move.obj
        if (objOld!!.isHint || objOld.toString() == objNew.toString()) return false
        set(p, objNew)
        updateIsSolved()
        return true
    }

    fun switchObject(move: CarpentersWallGameMove?): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: CarpentersWallObject? ->
            if (obj is CarpentersWallEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) CarpentersWallMarkerObject() else CarpentersWallWallObject()
            if (obj is CarpentersWallWallObject) return@label if (markerOption == MarkerOptions.MarkerLast) CarpentersWallMarkerObject() else CarpentersWallEmptyObject()
            if (obj is CarpentersWallMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) CarpentersWallWallObject() else CarpentersWallEmptyObject()
            obj
        }
        val objOld = get(move!!.p)
        if (objOld!!.isHint) return false
        move.obj = f.f(objOld)
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
        for (r in 0 until rows() - 1) rule2x2@ for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            for (os in CarpentersWallGame.Companion.offset2) if (get(p.add(os)) !is CarpentersWallWallObject) continue@rule2x2
            isSolved = false
        }
        val g = Graph()
        val pos2node: MutableMap<Position, Node> = HashMap()
        val rngWalls: MutableList<Position> = ArrayList()
        var rngEmpty: MutableList<Position> = ArrayList()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
            val o = get(p)
            if (o is CarpentersWallWallObject) rngWalls.add(p) else rngEmpty.add(p)
            if (o is CarpentersWallCornerObject) o.state = HintState.Normal else if (o is CarpentersWallLeftObject) o.state = HintState.Normal else if (o is CarpentersWallRightObject) o.state = HintState.Normal else if (o is CarpentersWallUpObject) o.state = HintState.Normal else if (o is CarpentersWallDownObject) o.state = HintState.Normal
        }
        for (p in rngWalls) for (os in CarpentersWallGame.Companion.offset) {
            val p2 = p.add(os)
            if (rngWalls.contains(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        for (p in rngEmpty) for (os in CarpentersWallGame.Companion.offset) {
            val p2 = p.add(os)
            if (rngEmpty.contains(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        if (rngWalls.isEmpty()) isSolved = false else {
            // The garden is separated by a single continuous wall. This means all
            // wall tiles on the board must be connected horizontally or vertically.
            // There can't be isolated walls.
            g.setRootNode(pos2node[rngWalls[0]])
            val nodeList = g.bfs()
            if (rngWalls.size != nodeList.size) isSolved = false
        }
        while (!rngEmpty.isEmpty()) {
            val node = pos2node[rngEmpty[0]]
            g.setRootNode(node)
            val nodeList = g.bfs()
            val area = fj.data.HashMap.fromMap(pos2node).toStream().filter { e: P2<Position, Node> -> nodeList.contains(e._2()) }.map { e: P2<Position, Node> -> e._1() }.toJavaList()
            rngEmpty = List.iterableList(rngEmpty).removeAll { p: Position -> nodeList.contains(pos2node[p]) }.toJavaList()
            val rngHint = List.iterableList(area).filter { p: Position? -> get(p)!!.isHint }.toJavaList()
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
            val r12 = r1
            val r22 = r2
            val c12 = c1
            val c22 = c2
            val cntR1 = List.iterableList(area).filter { p: Position -> p.row == r12 }.length()
            val cntR2 = List.iterableList(area).filter { p: Position -> p.row == r22 }.length()
            val cntC1 = List.iterableList(area).filter { p: Position -> p.col == c12 }.length()
            val cntC2 = List.iterableList(area).filter { p: Position -> p.col == c22 }.length()
            val f = F2 { a: Int, b: Int -> a > 1 && b > 1 && a + b - 1 == n1 }
            // 2. In the end, the empty spaces left by the Nurikabe will form many Carpenter's
            // Squares (L shaped tools) of different size.
            val squareType = if (f.f(cntR1, cntC1)) 0 else  // ┌
                if (f.f(cntR1, cntC2)) 1 else  // ┐
                    if (f.f(cntR2, cntC1)) 2 else  // └
                        if (f.f(cntR2, cntC2)) 3 else -1 // ┘
            for (p in rngHint) {
                val o = get(p)
                if (o is CarpentersWallCornerObject) {
                    // 3. The circled numbers on the board indicate the corner of the L.
                    // 4. When a number is inside the circle, that indicates the total number of
                    // squares occupied by the L.
                    val o2 = o
                    val n2 = o2.tiles
                    val s = if (squareType == -1) HintState.Normal else if (!(n1 == n2 || n2 == 0)) HintState.Error else if (squareType == 0 && p == Position(r1, c1) || squareType == 1 && p == Position(r1, c2) || squareType == 2 && p == Position(r2, c1) || squareType == 3 && p == Position(r2, c2)) HintState.Complete else HintState.Error
                    o2.state = s
                    if (s != HintState.Complete) isSolved = false
                } else if (o is CarpentersWallLeftObject) {
                    // 5. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    val s = if (squareType == -1) HintState.Normal else if (squareType == 0 && p == Position(r1, c2) ||
                        squareType == 2 && p == Position(r2, c2)) HintState.Complete else HintState.Error
                    o.state = s
                    if (s != HintState.Complete) isSolved = false
                } else if (o is CarpentersWallUpObject) {
                    // 5. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    val s = if (squareType == -1) HintState.Normal else if (squareType == 0 && p == Position(r2, c1) ||
                        squareType == 1 && p == Position(r2, c2)) HintState.Complete else HintState.Error
                    o.state = s
                    if (s != HintState.Complete) isSolved = false
                } else if (o is CarpentersWallRightObject) {
                    // 5. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    val s = if (squareType == -1) HintState.Normal else if (squareType == 1 && p == Position(r1, c1) ||
                        squareType == 3 && p == Position(r2, c1)) HintState.Complete else HintState.Error
                    o.state = s
                    if (s != HintState.Complete) isSolved = false
                } else if (o is CarpentersWallDownObject) {
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

    init {
        objArray = arrayOfNulls(rows() * cols())
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.size)
    }
}