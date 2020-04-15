package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import fj.data.HashMap
import fj.data.Stream
import java.util.*

class TapAlikeGameState(game: TapAlikeGame) : CellsGameState<TapAlikeGame?, TapAlikeGameMove?, TapAlikeGameState?>(game) {
    var objArray: Array<TapAlikeObject?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: TapAlikeObject?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: TapAlikeObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: TapAlikeGameMove): Boolean {
        val p: Position = move.p
        val objOld: TapAlikeObject? = get(p)
        val objNew: TapAlikeObject = move.obj
        if (objOld is TapAlikeHintObject || objOld == objNew) return false
        set(p, objNew)
        updateIsSolved()
        return true
    }

    fun switchObject(move: TapAlikeGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val f: F<TapAlikeObject, TapAlikeObject> = label@ F<TapAlikeObject, TapAlikeObject> { obj: TapAlikeObject? ->
            if (obj is TapAlikeEmptyObject) return@label if (markerOption == MarkerOptions.MarkerFirst) TapAlikeMarkerObject() else TapAlikeWallObject()
            if (obj is TapAlikeWallObject) return@label if (markerOption == MarkerOptions.MarkerLast) TapAlikeMarkerObject() else TapAlikeEmptyObject()
            if (obj is TapAlikeMarkerObject) return@label if (markerOption == MarkerOptions.MarkerFirst) TapAlikeWallObject() else TapAlikeEmptyObject()
            obj
        }
        move.obj = f.f(get(move.p))
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 10/Tap-Alike

        Summary
        Dr. Jekyll and Mr. Tapa

        Description
        1. Plays with the same rules as Tapa with these variations:
        2. At the end of the solution, the filled tiles will form an identical
           pattern to the one formed by the empty tiles.
        3. It's basically like having the same figure rotated or reversed in the
           opposite colour. The two figures will have the same exact shape.
    */
    private fun updateIsSolved() {
        isSolved = true
        // A number indicates how many of the surrounding tiles are filled. If a
        // tile has more than one number, it hints at multiple separated groups
        // of filled tiles.
        val computeHint: F<List<Int>, List<Int>> = F<List<Int>, List<Int>> { filled: List<Int> ->
            val hint = mutableListOf<Int>()
            if (filled.isEmpty()) hint.add(0) else {
                for (j in filled.indices) if (j == 0 || filled[j] - filled[j - 1] != 1) hint.add(1) else hint[hint.size - 1] = hint[hint.size - 1] + 1
                if (filled.size > 1 && hint.size > 1 && filled[filled.size - 1] - filled[0] == 7) {
                    hint[0] = hint[0] + hint[hint.size - 1]
                    hint.removeAt(hint.size - 1)
                }
                Collections.sort(hint)
                // hint.sort(Comparator.naturalOrder());
            }
            hint
        }
        val isCompatible: F2<List<Int>, List<Int>, Boolean> = label@ F2<List<Int>, List<Int>, Boolean> { computedHint: List<Int>, givenHint: List<Int> ->
            if (computedHint == givenHint) return@label true
            if (computedHint.size != givenHint.size) return@label false
            val h1: Set<Int> = HashSet(computedHint)
            val h2: MutableSet<Int> = HashSet(givenHint)
            h2.remove(-1)
            h1.containsAll(h2)
        }
        HashMap.fromMap<Position, List<Int>>(game.pos2hint).foreachDoEffect(Effect1<P2<Position, List<Int>>> { kv: P2<Position?, List<Int?>?> ->
            val p: Position = kv._1()
            val arr2: List<Int> = kv._2()
            val filled = Stream.range(0, 8).filter(F<Int, Boolean> { i: Int? ->
                val p2 = p.add(TapAlikeGame.Companion.offset.get(i!!))
                isValid(p2) && get(p2) is TapAlikeWallObject
            }).toJavaList()
            val arr: List<Int> = computeHint.f(filled)
            val s: HintState = if (arr.size == 1 && arr[0] == 0) HintState.Normal else if (isCompatible.f(arr, arr2)) HintState.Complete else HintState.Error
            val o = TapAlikeHintObject()
            o.state = s
            set(p, o)
            if (s != HintState.Complete) isSolved = false
        })
        if (!isSolved) return
        // Filled tiles can't cover an area of 2*2 or larger (just like Nurikabe).
        // Tiles with numbers can be considered 'empty'.
        for (r in 0 until rows() - 1) for (c in 0 until cols() - 1) {
            val p = Position(r, c)
            if (fj.data.Array.array<Position>(*TapAlikeGame.Companion.offset2).forall(F<Position, Boolean> { os: Position? ->
                    val o: TapAlikeObject? = get(p.add(os))
                    o is TapAlikeWallObject
                })) {
                isSolved = false
                return
            }
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            if (get(p) is TapAlikeWallObject) {
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
        // 2. At the end of the solution, the filled tiles will form an identical
        // pattern to the one formed by the empty tiles.
        // 3. It's basically like having the same figure rotated or reversed in the
        // opposite colour. The two figures will have the same exact shape.
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val o1: TapAlikeObject? = get(r, c)
            val o2: TapAlikeObject? = get(rows() - 1 - r, cols() - 1 - c)
            if (o1 is TapAlikeWallObject == o2 is TapAlikeWallObject) {
                isSolved = false
                return
            }
        }
    }

    init {
        objArray = arrayOfNulls<TapAlikeObject>(rows() * cols())
        for (i in objArray.indices) objArray[i] = TapAlikeEmptyObject()
        for (p in game.pos2hint.keys) set(p, TapAlikeHintObject())
    }
}