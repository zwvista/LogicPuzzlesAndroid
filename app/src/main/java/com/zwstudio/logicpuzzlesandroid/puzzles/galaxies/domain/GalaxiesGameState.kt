package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.P2
import java.util.*

class GalaxiesGameState(game: GalaxiesGame) : CellsGameState<GalaxiesGame, GalaxiesGameMove, GalaxiesGameState>(game) {
    var objArray: Array<Array<GridLineObject?>?>?
    var pos2state = mutableMapOf<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray!![row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject?>?) {
        objArray!![row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: Array<GridLineObject?>?) {
        set(p.row, p.col, obj)
    }

    fun setObject(move: GalaxiesGameMove): Boolean {
        val p1 = move!!.p
        val dir = move.dir
        val dir2 = (dir + 2) % 4
        if (game!![p1][dir] != GridLineObject.Empty) return false
        val o = get(p1)!![dir]
        if (o == move.obj) return false
        val p2 = p1!!.add(GalaxiesGame.Companion.offset.get(dir))
        get(p1)!![dir] = move.obj
        get(p2)!![dir2] = get(p1)!![dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: GalaxiesGameMove): Boolean {
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
        iOS Game: Logic Games/Puzzle Set 4/Galaxies

        Summary
        Fill the Symmetric Spiral Galaxies

        Description
        1. In the board there are marked centers of a few 'Spiral' Galaxies.
        2. These Galaxies are symmetrical to a rotation of 180 degrees. This
           means that rotating the shape of the Galaxy by 180 degrees (half a
           full turn) around the center, will result in an identical shape.
        3. In the end, all the space must be included in Galaxies and Galaxies
           can't overlap.
        4. There can be single tile Galaxies (with the center inside it) and
           some Galaxy center will be cross two or four tiles.
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
            for (i in 0..3) if (get(p.add(GalaxiesGame.Companion.offset2.get(i)))!![GalaxiesGame.Companion.dirs.get(i)] != GridLineObject.Line) g.connectNode(pos2node[p], pos2node[p.add(GalaxiesGame.Companion.offset.get(i))])
        }
        val areas = mutableListOf<List<Position>>()
        val pos2area = mutableMapOf<Position, Int>()
        while (!pos2node.isEmpty()) {
            g.setRootNode(fj.data.List.iterableList(pos2node.values).head())
            val nodeList = g.bfs()
            val area = fj.data.HashMap.fromMap(pos2node).toStream().filter { e: P2<Position, Node> -> nodeList.contains(e._2()) }.map { e: P2<Position, Node> -> e._1() }.toJavaList()
            areas.add(area)
            for (p in area) {
                pos2area[p] = areas.size
                pos2node.remove(p)
            }
        }
        var n1 = 0
        for (area in areas) {
            val rng = fj.data.List.iterableList(game!!.galaxies).filter { p: Position -> area.contains(Position(p!!.row / 2, p.col / 2)) }.toJavaList()
            if (rng.size != 1) {
                // 3. Galaxies can't overlap.
                for (p in rng) pos2state[p] = HintState.Normal
                isSolved = false
            } else {
                // 2. These Galaxies are symmetrical to a rotation of 180 degrees. This
                // means that rotating the shape of the Galaxy by 180 degrees (half a
                // full turn) around the center, will result in an identical shape.
                val galaxy = rng[0]
                val b = fj.data.List.iterableList(area).forall { p: Position -> area.contains(Position(galaxy!!.row - p.row - 1, galaxy.col - p.col - 1)) }
                val s = if (b) HintState.Complete else HintState.Error
                pos2state[galaxy] = s
                if (!b) isSolved = false
            }
            n1 += area.size
        }
        // 3. In the end, all the space must be included in Galaxies
        if (n1 != rows() * cols()) isSolved = false
    }

    init {
        objArray = Cloner().deepClone(game.objArray)
        for (p in game.galaxies) pos2state[p] = HintState.Normal
        updateIsSolved()
    }
}