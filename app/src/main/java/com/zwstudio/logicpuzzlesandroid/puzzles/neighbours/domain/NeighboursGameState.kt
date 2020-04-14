package com.zwstudio.logicpuzzlesandroid.puzzles.neighbours.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Graph
import com.zwstudio.logicpuzzlesandroid.common.domain.Node
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import java.util.*

class NeighboursGameState(game: NeighboursGame) : CellsGameState<NeighboursGame?, NeighboursGameMove?, NeighboursGameState?>(game) {
    var objArray: Array<Array<GridLineObject?>>
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position?) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: Array<GridLineObject?>) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: Array<GridLineObject?>) {
        set(p.row, p.col, obj)
    }

    fun setObject(move: NeighboursGameMove): Boolean {
        val p1: Position = move.p
        val dir: Int = move.dir
        val dir2 = (dir + 2) % 4
        if (game.get(p1).get(dir) != GridLineObject.Empty) return false
        val o: GridLineObject? = get(p1)[dir]
        if (o == move.obj) return false
        val p2 = p1.add(NeighboursGame.Companion.offset.get(dir))
        get(p1)[dir] = move.obj
        get(p2)[dir2] = get(p1)[dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: NeighboursGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<GridLineObject, GridLineObject> = label@ F<GridLineObject, GridLineObject> { obj: GridLineObject? ->
            when (obj) {
                GridLineObject.Empty -> return@label if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
                GridLineObject.Line -> return@label if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
                GridLineObject.Marker -> return@label if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            }
            obj
        }
        val dotObj: Array<GridLineObject?> = get(move.p)
        move.obj = f.f(dotObj[move.dir])
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 8/Neighbours

        Summary
        Neighbours, yes, but not equally sociable

        Description
        1. The board represents a piece of land bought by a bunch of people. They
           decided to split the land in equal parts.
        2. However some people are more social and some are less, so each owner
           wants an exact number of neighbours around him.
        3. Each number on the board represents an owner house and the number of
           neighbours he desires.
        4. Divide the land so that each one has an equal number of squares and
           the requested number of neighbours.
        5. Later on, there will be Question Marks, which represents an owner for
           which you don't know the neighbours preference.
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
            for (i in 0..3) if (get(p.add(NeighboursGame.Companion.offset2.get(i)))[NeighboursGame.Companion.dirs.get(i)] != GridLineObject.Line) g.connectNode(pos2node[p], pos2node[p.add(NeighboursGame.Companion.offset.get(i))])
        }
        val areas = mutableListOf<List<Position>>()
        val pos2area = mutableMapOf<Position, Int>()
        while (!pos2node.isEmpty()) {
            g.setRootNode(fj.data.List.iterableList(pos2node.values).head())
            val nodeList = g.bfs()
            val area = fj.data.HashMap.fromMap(pos2node).toStream().filter(F<P2<Position, Node>, Boolean> { e: P2<Position?, Node?> -> nodeList.contains(e._2()) }).map<Position>(F<P2<Position, Node>, Position> { e: P2<Position?, Node?> -> e._1() }).toJavaList()
            areas.add(area)
            for (p in area) {
                pos2area[p] = areas.size
                pos2node.remove(p)
            }
        }
        val n2: Int = game.areaSize
        for (area in areas) {
            val rng = fj.data.List.iterableList(area).filter(F<Position, Boolean> { p: Position? -> game.pos2hint.containsKey(p) }).toJavaList()
            if (rng.size != 1) {
                for (p in rng) pos2state[p] = HintState.Normal
                isSolved = false
                continue
            }
            val p3 = rng[0]
            val n1 = area.size
            val n3: Int = game.pos2hint.get(p3)
            val neighbours: F0<Int> = label@ F0<Int> {
                val indexes = mutableSetOf<Int>()
                val idx = pos2area[area[0]]
                for (p in area) {
                    var i = 0
                    while (i < 4) {
                        if (get(p.add(NeighboursGame.Companion.offset2.get(i)))[NeighboursGame.Companion.dirs.get(i)] != GridLineObject.Line) {
                            i++
                            continue
                        }
                        val p2 = p.add(NeighboursGame.Companion.offset.get(i))
                        val idx2 = pos2area[p2]
                        if (idx2 == null) {
                            i++
                            continue
                        }
                        if (idx == idx2) return@label -1
                        indexes.add(idx2)
                        i++
                    }
                }
                indexes.size
            }
            // 3. Each number on the board represents an owner house and the number of
            // neighbours he desires.
            // 4. Divide the land so that each one has an equal number of squares and
            // the requested number of neighbours.
            val s: HintState = if (n1 == n2 && n3 == neighbours.f()) HintState.Complete else HintState.Error
            pos2state[p3] = s
            if (s != HintState.Complete) isSolved = false
        }
    }

    init {
        objArray = Cloner().deepClone(game.objArray)
        updateIsSolved()
    }
}