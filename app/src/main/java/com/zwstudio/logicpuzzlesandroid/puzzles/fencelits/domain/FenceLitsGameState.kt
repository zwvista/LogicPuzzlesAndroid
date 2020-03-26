package com.zwstudio.logicpuzzlesandroid.puzzles.fencelits.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.Ord
import fj.P2
import fj.data.List
import java.util.*

class FenceLitsGameState(game: FenceLitsGame) : CellsGameState<FenceLitsGame?, FenceLitsGameMove?, FenceLitsGameState?>(game) {
    var objArray: Array<Array<GridLineObject?>?>?
    var pos2state: MutableMap<Position?, HintState?> = HashMap()
    operator fun get(row: Int, col: Int): Array<GridLineObject?>? {
        return objArray!![row * cols() + col]
    }

    operator fun get(p: Position?): Array<GridLineObject?>? {
        return get(p!!.row, p.col)
    }

    fun setObject(move: FenceLitsGameMove?): Boolean {
        val p1 = move!!.p
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        val o = get(p1)!![dir]
        if (o == move.obj) return false
        val p2 = p1!!.add(FenceLitsGame.Companion.offset.get(dir))
        get(p1)!![dir] = move.obj
        get(p2)!![dir2] = get(p1)!![dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: FenceLitsGameMove?): Boolean {
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
        iOS Game: Logic Games/Puzzle Set 12/FenceLits

        Summary
        Fencing Tetris

        Description
        1. The goal is to divide the board into Tetris pieces, including the
           square one (differently from LITS).
        2. The number in a cell tells you how many of the sides are marked
           (like slitherlink).
        3. Please consider that the outside border of the board as marked.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 2. The number in a cell tells you how many of the sides are marked
        // (like slitherlink).
        for ((p, n2) in game!!.pos2hint) {
            var n1 = 0
            for (i in 0..3) if (get(p!!.add(FenceLitsGame.Companion.offset2.get(i)))!![FenceLitsGame.Companion.dirs.get(i)] == GridLineObject.Line) n1++
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (n1 != n2) isSolved = false
        }
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
            for (i in 0..3) if (get(p.add(FenceLitsGame.Companion.offset2.get(i)))!![FenceLitsGame.Companion.dirs.get(i)] != GridLineObject.Line) g.connectNode(pos2node[p], pos2node[p.add(FenceLitsGame.Companion.offset.get(i))])
        }
        if (!isSolved) return
        // 1. The goal is to divide the board into Tetris pieces, including the
        // square one (differently from LITS).
        while (!pos2node.isEmpty()) {
            g.setRootNode(fj.data.HashMap.fromMap(pos2node).values().head())
            val nodeList = g.bfs()
            val area = fj.data.HashMap.fromMap(pos2node).toStream().filter { e: P2<Position, Node> -> nodeList.contains(e._2()) }.map { e: P2<Position, Node> -> e._1() }.toJavaList()
            if (area.size != 4) {
                isSolved = false
                return
            }
            Collections.sort(area) { obj: Position, other: Position? -> obj.compareTo(other) }
            val treeOffsets: MutableList<Position> = ArrayList()
            val p2 = Position(List.iterableList(area).map { p: Position -> p.row }.minimum(Ord.intOrd),
                List.iterableList(area).map { p: Position -> p.col }.minimum(Ord.intOrd))
            for (p in area) treeOffsets.add(p.subtract(p2))
            if (!fj.data.Array.array<Array<Position>>(*FenceLitsGame.Companion.tetrominoes).exists { arr: Array<Position>? -> Arrays.equals(arr, treeOffsets.toTypedArray()) }) {
                isSolved = false
                return
            }
            for (p in area) pos2node.remove(p)
        }
    }

    init {
        objArray = Cloner().deepClone(game.objArray)
        updateIsSolved()
    }
}