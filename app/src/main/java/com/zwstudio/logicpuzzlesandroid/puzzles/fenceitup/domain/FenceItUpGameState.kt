package com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.P2
import fj.data.List
import java.util.*

class FenceItUpGameState(game: FenceItUpGame) : CellsGameState<FenceItUpGame?, FenceItUpGameMove?, FenceItUpGameState?>(game) {
    var objArray: Array<Array<GridLineObject?>?>?
    var pos2state = mutableMapOf<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray!![row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    fun setObject(move: FenceItUpGameMove?): Boolean {
        val p1 = move!!.p
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        if (game!![p1][dir] != GridLineObject.Empty) return false
        val o = get(p1)!![dir]
        if (o == move.obj) return false
        val p2 = p1!!.add(FenceItUpGame.Companion.offset.get(dir))
        get(p1)!![dir] = move.obj
        get(p2)!![dir2] = get(p1)!![dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: FenceItUpGameMove?): Boolean {
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
        iOS Game: Logic Games/Puzzle Set 7/Fence It Up

        Summary
        Now with Fences

        Description
        1. A simple puzzle where you have to divide the Board into enclosed
           areas by Fences.
        2. Each area must contain one number and the number tells you the length
           of the perimeter of the area.
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
            for (i in 0..3) if (get(p.add(FenceItUpGame.Companion.offset2.get(i)))!![FenceItUpGame.Companion.dirs.get(i)] != GridLineObject.Line) g.connectNode(pos2node[p], pos2node[p.add(FenceItUpGame.Companion.offset.get(i))])
        }
        while (!pos2node.isEmpty()) {
            g.setRootNode(List.iterableList(pos2node.values).head())
            val nodeList = g.bfs()
            val area = fj.data.HashMap.fromMap(pos2node).toStream().filter { e: P2<Position, Node> -> nodeList.contains(e._2()) }.map { e: P2<Position, Node> -> e._1() }.toJavaList()
            for (p in area) pos2node.remove(p)
            val rng = List.iterableList(area).filter { p: Position -> game!!.pos2hint.containsKey(p) }.toJavaList()
            if (rng.size != 1) {
                for (p in rng) pos2state[p] = HintState.Normal
                isSolved = false
                continue
            }
            val p2 = rng[0]
            var n1 = 0
            val n2 = game!!.pos2hint[p2]!!
            // 2. Each area must contain one number and the number tells you the length
            // of the perimeter of the area.
            for (p in area) for (i in 0..3) if (get(p.add(FenceItUpGame.Companion.offset2.get(i)))!![FenceItUpGame.Companion.dirs.get(i)] == GridLineObject.Line) n1++
            val s = if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p2] = s
            if (s != HintState.Complete) isSolved = false
        }
    }

    init {
        objArray = Cloner().deepClone(game.objArray)
        updateIsSolved()
    }
}