package com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.F0
import fj.P2
import fj.data.List
import java.util.*

class BoxItAroundGameState(game: BoxItAroundGame) : CellsGameState<BoxItAroundGame?, BoxItAroundGameMove?, BoxItAroundGameState?>(game) {
    var objArray: Array<Array<GridLineObject?>?>?
    var pos2state = mutableMapOf<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray!![row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    fun setObject(move: BoxItAroundGameMove?): Boolean {
        val p1 = move!!.p
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        if (game!![p1][dir] != GridLineObject.Empty) return false
        val o = get(p1)!![dir]
        if (o == move.obj) return false
        val p2 = p1!!.add(BoxItAroundGame.Companion.offset.get(dir))
        get(p1)!![dir] = move.obj
        get(p2)!![dir2] = get(p1)!![dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: BoxItAroundGameMove?): Boolean {
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
        iOS Game: Logic Games/Puzzle Set 8/Box It Around

        Summary
        Keep Boxing Stuff

        Description
        1. A simple puzzle where you have to divide the Board in Boxes (Rectangles).
        2. Each Box must contain one number and the number represents the sum of
           the width and the height of that Box.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
        }
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            for (i in 0..3) if (get(p.add(BoxItAroundGame.Companion.offset2.get(i)))!![BoxItAroundGame.Companion.dirs.get(i)] != GridLineObject.Line) g.connectNode(pos2node[p], pos2node[p.add(BoxItAroundGame.Companion.offset.get(i))])
        }
        while (!pos2node.isEmpty()) {
            g.setRootNode(List.iterableList(pos2node.values).head())
            val nodeList = g.bfs()
            val area = fj.data.HashMap.fromMap(pos2node).toStream().filter { e: P2<Position, Node> -> nodeList.contains(e._2()) }.map { e: P2<Position, Node> -> e._1() }.toJavaList()
            for (p in area) pos2node.remove(p)
            val rng = List.iterableList(area).filter { p: Position -> game!!.pos2hint.containsKey(p) }.toJavaList()
            // 2. Each Box must contain one number.
            if (rng.size != 1) {
                for (p in rng) pos2state[p] = HintState.Normal
                isSolved = false
                continue
            }
            val p2 = rng[0]
            val n1 = area.size
            val n2 = game!!.pos2hint[p2]!!
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
            val rs = r2 - r1 + 1
            val cs = c2 - c1 + 1
            val r11 = r1
            val r22 = r2
            val c11 = c1
            val c22 = c2
            val hasLine = label@ F0 {
                var r = r11
                while (r <= r22) {
                    var c = c11
                    while (c <= c22) {
                        val dotObj = get(r + 1, c + 1)
                        if (r < r22 && dotObj!![3] == GridLineObject.Line || c < c22 && dotObj!![0] == GridLineObject.Line) return@label true
                        c++
                    }
                    r++
                }
                false
            }
            // 1. A simple puzzle where you have to divide the Board in Boxes (Rectangles).
            // 2. The number represents the sum of the width and the height of that Box.
            val s = if (rs * cs == n1 && rs + cs == n2 && !hasLine.f()) HintState.Complete else HintState.Error
            pos2state[p2] = s
            if (s != HintState.Complete) isSolved = false
        }
    }

    init {
        objArray = Cloner().deepClone(game.objArray)
        updateIsSolved()
    }
}