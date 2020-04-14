package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGame
import fj.F
import fj.F2
import fj.P2
import fj.data.HashMap
import fj.data.Stream
import fj.function.Integers
import java.util.*

class TapARowGameState(game: TapARowGame) : CellsGameState<TapARowGame?, TapARowGameMove?, TapARowGameState?>(game) {
    var objArray: Array<TapARowObject?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position?) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: TapARowObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position?, obj: TapARowObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: TapARowGameMove?): Boolean {
        val p = move!!.p
        val objOld = get(p)
        val objNew = move.obj
        if (objOld is TapARowHintObject || objOld == objNew) return false
        set(p, objNew)
        updateIsSolved()
        return true
    }

    fun switchObject(move: TapARowGameMove?): Boolean {
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: TapARowObject? ->
            if (obj is TapARowEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) TapARowMarkerObject() else TapARowWallObject()
            if (obj is TapARowWallObject) return@label if (markerOption == MarkerOptions.MarkerLast) TapARowMarkerObject() else TapARowEmptyObject()
            if (obj is TapARowMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) TapARowWallObject() else TapARowEmptyObject()
            obj
        }
        move!!.obj = f.f(get(move.p))
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 10/Tap-A-Row

        Summary
        Tap me a row, please

        Description
        1. Plays with the same rules as Tapa with these variations:
        2. The number also tells you the filled cell count for that row.
        3. In other words, the sum of the digits in that row equals the number
           of that row.
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
                val p2 = p!!.add(TapARowGame.Companion.offset.get(i!!))
                isValid(p2) && get(p2) is TapARowWallObject
            }.toJavaList()
            val arr = computeHint.f(filled)
            val s = if (arr.size == 1 && arr[0] == 0) HintState.Normal else if (isCompatible.f(arr, arr2)) HintState.Complete else HintState.Error
            val o = TapARowHintObject()
            o.state = s
            set(p, o)
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        // Filled tiles can't cover an area of 2*2 or larger (just like Nurikabe).
        // Tiles with numbers can be considered 'empty'.
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            if (fj.data.Array.array<Position>(*TapARowGame.Companion.offset2).forall { os: Position? ->
                    val o = get(p.add(os))
                    o is TapARowWallObject
                }) {
                isSolved = false
                return
            }
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        val rngWalls: List<Position> = ArrayList()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            if (get(p) is TapARowWallObject) {
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for ((p, node) in pos2node) {
            for (os in TapaGame.Companion.offset) {
                val p2 = p.add(os)
                val node2 = pos2node[p2]
                if (node2 != null) g.connectNode(node, node2)
            }
        }
        // The goal is to fill some tiles forming a single orthogonally continuous
        // path. Just like Nurikabe.
        g.setRootNode(fj.data.List.iterableList(pos2node.values).head())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
        // 2. The number also tells you the filled cell count for that row.
        // 3. In other words, the sum of the digits in that row equals the number
        // of that row.
        for (r in 0 until rows()) {
            var n1 = 0
            var n2 = 0
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val o = get(p)
                if (o is TapARowWallObject) n1++ else if (o is TapARowHintObject) {
                    val arr = game!!.pos2hint[p]
                    n2 += fj.data.Array.iterableArray(arr).foldLeft(Integers.add, 0)
                }
            }
            if (n2 != 0 && n1 != n2) {
                isSolved = false
                return
            }
        }
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        for (i in objArray.indices) objArray[i] = TapARowEmptyObject()
        for (p in game.pos2hint.keys) set(p, TapARowHintObject())
    }
}