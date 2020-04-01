package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.F2
import fj.P2
import fj.data.List
import java.util.*

class CarpentersSquareGameState(game: CarpentersSquareGame) : CellsGameState<CarpentersSquareGame, CarpentersSquareGameMove, CarpentersSquareGameState>(game) {
    var objArray: Array<Array<GridLineObject?>?>?
    var pos2state: MutableMap<Position?, HintState?> = HashMap()
    operator fun get(row: Int, col: Int): Array<GridLineObject?>? {
        return objArray!![row * cols() + col]
    }

    operator fun get(p: Position?): Array<GridLineObject?>? {
        return get(p!!.row, p.col)
    }

    fun setObject(move: CarpentersSquareGameMove?): Boolean {
        val p1 = move!!.p
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        if (game!![p1][dir] != GridLineObject.Empty) return false
        val o = get(p1)!![dir]
        if (o == move.obj) return false
        val p2 = p1!!.add(CarpentersSquareGame.Companion.offset.get(dir))
        get(p1)!![dir] = move.obj
        get(p2)!![dir2] = get(p1)!![dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: CarpentersSquareGameMove?): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: GridLineObject? ->
            when (obj) {
                GridLineObject.Empty -> return@label if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
                GridLineObject.Line -> return@label if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
                GridLineObject.Marker -> return@label if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            }
            obj
        }
        val dotObj = get(move!!.p)
        move.obj = f.f(dotObj!![move.dir])
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
        val pos2node: MutableMap<Position, Node> = HashMap()
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
        }
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            for (i in 0..3) if (get(p.add(CarpentersSquareGame.Companion.offset2.get(i)))!![CarpentersSquareGame.Companion.dirs.get(i)] != GridLineObject.Line) g.connectNode(pos2node[p], pos2node[p.add(CarpentersSquareGame.Companion.offset.get(i))])
        }
        while (!pos2node.isEmpty()) {
            g.setRootNode(List.iterableList(pos2node.values).head())
            val nodeList = g.bfs()
            val area = fj.data.HashMap.fromMap(pos2node).toStream().filter { e: P2<Position, Node> -> nodeList.contains(e._2()) }.map { e: P2<Position, Node> -> e._1() }.toJavaList()
            for (p in area) pos2node.remove(p)
            val rngHint = List.iterableList(area).filter { p: Position? -> game!!.pos2hint.containsKey(p) }.toJavaList()
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
            // 1. You just have to divide the board into many.Capenter's Squares (L shaped tools) of different size.
            val squareType = if (f.f(cntR1, cntC1)) 0 else  // ┌
                if (f.f(cntR1, cntC2)) 1 else  // ┐
                    if (f.f(cntR2, cntC1)) 2 else  // └
                        if (f.f(cntR2, cntC2)) 3 else -1 // ┘
            // 5. All the tiles in the board have to be part of a Carpenter's Square.
            if (squareType == -1) isSolved = false
            for (p in rngHint) {
                val o = game!!.pos2hint[p]
                if (o is CarpentersSquareCornerHint) {
                    // 2. The circled numbers on the board indicate the corner of the L.
                    // 3. When a number is inside the circle, that indicates the total number of
                    // squares occupied by the L.
                    val n2 = o.tiles
                    val s = if (squareType == -1) HintState.Normal else if (!(n1 == n2 || n2 == 0)) HintState.Error else if (squareType == 0 && p == Position(r1, c1) || squareType == 1 && p == Position(r1, c2) || squareType == 2 && p == Position(r2, c1) || squareType == 3 && p == Position(r2, c2)) HintState.Complete else HintState.Error
                    pos2state[p] = s
                    if (s != HintState.Complete) isSolved = false
                } else if (o is CarpentersSquareLeftHint) {
                    // 4. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    val s = if (squareType == -1) HintState.Normal else if (squareType == 0 && p == Position(r1, c2) ||
                        squareType == 2 && p == Position(r2, c2)) HintState.Complete else HintState.Error
                    pos2state[p] = s
                    if (s != HintState.Complete) isSolved = false
                } else if (o is CarpentersSquareUpHint) {
                    // 4. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    val s = if (squareType == -1) HintState.Normal else if (squareType == 0 && p == Position(r2, c1) ||
                        squareType == 1 && p == Position(r2, c2)) HintState.Complete else HintState.Error
                    pos2state[p] = s
                    if (s != HintState.Complete) isSolved = false
                } else if (o is CarpentersSquareRightHint) {
                    // 4. The arrow always sits at the end of an arm and points to the corner of
                    // an L.
                    val s = if (squareType == -1) HintState.Normal else if (squareType == 1 && p == Position(r1, c1) ||
                        squareType == 3 && p == Position(r2, c1)) HintState.Complete else HintState.Error
                    pos2state[p] = s
                    if (s != HintState.Complete) isSolved = false
                } else if (o is CarpentersSquareDownHint) {
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

    init {
        objArray = Cloner().deepClone(game.objArray)
        updateIsSolved()
    }
}