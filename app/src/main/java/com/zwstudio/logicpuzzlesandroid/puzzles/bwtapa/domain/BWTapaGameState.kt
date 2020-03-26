package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.F2
import fj.P2
import fj.data.HashMap
import fj.data.Stream
import java.util.*

class BWTapaGameState(game: BWTapaGame) : CellsGameState<BWTapaGame?, BWTapaGameMove?, BWTapaGameState?>(game) {
    var objArray: Array<BWTapaObject?>
    operator fun get(row: Int, col: Int): BWTapaObject? {
        return objArray[row * cols() + col]
    }

    operator fun get(p: Position?): BWTapaObject? {
        return get(p!!.row, p.col)
    }

    operator fun set(row: Int, col: Int, obj: BWTapaObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position?, obj: BWTapaObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: BWTapaGameMove?): Boolean {
        val p = move!!.p
        val objOld = get(p)
        val objNew = move.obj
        if (objOld is BWTapaHintObject || objOld == objNew) return false
        set(p, objNew)
        updateIsSolved()
        return true
    }

    fun switchObject(move: BWTapaGameMove?): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: BWTapaObject? ->
            if (obj is BWTapaEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) BWTapaMarkerObject() else BWTapaWallObject()
            if (obj is BWTapaWallObject) return@label if (markerOption == MarkerOptions.MarkerLast) BWTapaMarkerObject() else BWTapaEmptyObject()
            if (obj is BWTapaMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) BWTapaWallObject() else BWTapaEmptyObject()
            obj
        }
        move!!.obj = f.f(get(move.p))
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 9/B&W Tapa

        Summary
        Black and white Tapas

        Description
        1. Play with the same rules as Tapa with these variations:
        2. Both Black and White cells must form a single continuous region.
        3. There can't be any 2*2 of white or black cells.
    */
    private fun updateIsSolved() {
        isSolved = true
        // A number indicates how many of the surrounding tiles are filled. If a
        // tile has more than one number, it hints at multiple separated groups
        // of filled tiles.
        val computeHint = F<List<Int>, List<Int?>> { filled: List<Int> ->
            val hint: MutableList<Int?> = ArrayList()
            if (filled.isEmpty()) hint.add(0) else {
                for (j in filled.indices) if (j == 0 || filled[j] - filled[j - 1] != 1) hint.add(1) else hint[hint.size - 1] = hint[hint.size - 1]!! + 1
                if (filled.size > 1 && hint.size > 1 && filled[filled.size - 1] - filled[0] == 7) {
                    hint[0] = hint[0]!! + hint[hint.size - 1]!!
                    hint.removeAt(hint.size - 1)
                }
                Collections.sort(hint)
                // hint.sort(Comparator.naturalOrder());
            }
            hint
        }
        val isCompatible = label@ F2 { computedHint: List<Int?>, givenHint: List<Int?>? ->
            if (computedHint == givenHint) return@label true
            if (computedHint.size != givenHint!!.size) return@label false
            val h1: Set<Int?> = HashSet(computedHint)
            val h2: MutableSet<Int?> = HashSet(givenHint)
            h2.remove(-1)
            h1.containsAll(h2)
        }
        HashMap.fromMap(game!!.pos2hint).foreachDoEffect { kv: P2<Position?, List<Int?>?> ->
            val p = kv._1()
            val arr2 = kv._2()
            val filled = Stream.range(0, 8).filter { i: Int? ->
                val p2 = p!!.add(BWTapaGame.Companion.offset.get(i!!))
                isValid(p2) && get(p2) is BWTapaWallObject
            }.toJavaList()
            val arr = computeHint.f(filled)
            val s = if (arr.size == 1 && arr[0] == 0) HintState.Normal else if (isCompatible.f(arr, arr2)) HintState.Complete else HintState.Error
            val o = BWTapaHintObject()
            o.state = s
            set(p, o)
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        // 3. There can't be any 2*2 of white or black cells.
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            if (fj.data.Array.array<Position>(*BWTapaGame.Companion.offset2).forall { os: Position? ->
                        val o = get(p.add(os))
                        o is BWTapaWallObject
                    } || fj.data.Array.array<Position>(*BWTapaGame.Companion.offset2).forall { os: Position? ->
                        val o = get(p.add(os))
                        o is BWTapaEmptyObject || o is BWTapaHintObject
                    }) {
                isSolved = false
                return
            }
        }
        val g = Graph()
        val pos2node: MutableMap<Position, Node> = HashMap()
        val rngWalls: MutableList<Position> = ArrayList()
        val rngEmpty: MutableList<Position> = ArrayList()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val node = Node(p.toString())
            g.addNode(node)
            pos2node[p] = node
            if (get(p) is BWTapaWallObject) rngWalls.add(p) else rngEmpty.add(p)
        }
        for (p in rngWalls) for (os in BWTapaGame.Companion.offset) {
            val p2 = p.add(os)
            if (rngWalls.contains(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        for (p in rngEmpty) for (os in BWTapaGame.Companion.offset) {
            val p2 = p.add(os)
            if (rngEmpty.contains(p2)) g.connectNode(pos2node[p], pos2node[p2])
        }
        // 2. Both Black and White cells must form a single continuous region.
        g.setRootNode(pos2node[rngWalls[0]])
        var nodeList = g.bfs()
        if (rngWalls.size != nodeList.size) {
            isSolved = false
            return
        }
        g.setRootNode(pos2node[rngEmpty[0]])
        nodeList = g.bfs()
        if (rngEmpty.size != nodeList.size) isSolved = false
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        for (i in objArray.indices) objArray[i] = BWTapaEmptyObject()
        for (p in game.pos2hint.keys) set(p, BWTapaHintObject())
    }
}